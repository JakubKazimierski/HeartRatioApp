package com.example.heartratioapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.heartratioapp.diagnose_activity.DiagnoseActivity
import com.example.heartratioapp.history_activity.HistoryActivity
import com.example.heartratioapp.measure_activity.MeasureActivity
import com.example.heartratioapp.model_classes.User
import com.example.heartratioapp.niotification.NotificationService
import com.example.heartratioapp.settings_activity.Settings
import com.example.heartratioapp.settings_activity.SettingsActivity
import com.example.heartratioapp.welcome_activity.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private var refUsers: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Settings.loadSettings(getSharedPreferences("HeartRatioSettings", Context.MODE_PRIVATE))

        val serviceIntent = Intent(this, NotificationService::class.java)
        startService(serviceIntent)
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers =
            FirebaseDatabase.getInstance("https://heartratioapp-default-rtdb.europe-west1.firebasedatabase.app/").reference.child(
                "Users"
            ).child(firebaseUser!!.uid)

        //display name and photo
        refUsers!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user: User? = snapshot.getValue(User::class.java)
                    findViewById<TextView>(R.id.user_name).text = user!!.username
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()

                return true
            }
        }

        return false
    }

    // Method for starting new MeasureActivity
    fun newMeasure(view: View) {
        val intent = Intent(this, MeasureActivity::class.java)
        intent.putExtra("user", firebaseUser)
        startActivity(intent)
    }

    // Method for starting HistoryActivity
    fun showHistory(view: View) {
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putExtra("user", firebaseUser)
        startActivity(intent)
    }

    fun openSettings(view: View) {
        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
        intent.putExtra("user", firebaseUser)
        startActivity(intent)
    }

//    fun openDiagnose(view: View) {
//        val intent = Intent(this@MainActivity, DiagnoseActivity::class.java)
//        intent.putExtra("user", firebaseUser)
//        startActivity(intent)
//    }

    override fun onDestroy() {
        super.onDestroy()
        Settings.saveSettings()
    }

    fun callEmergency(view: View) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:" + Settings.emergencyNumber)
        startActivity(dialIntent)
    }
}