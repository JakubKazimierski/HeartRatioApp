package com.example.heartratioapp.welcome_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.heartratioapp.MainActivity
import com.example.heartratioapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val toolbar: Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {

            val intent = Intent(this@RegisterActivity, WelcomeActivity::class.java)
            startActivity(intent)

            finish()
        }

        mAuth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.register_btn).setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val username = findViewById<EditText>(R.id.username_register).text.toString()
        val email = findViewById<EditText>(R.id.email_register).text.toString()
        val password = findViewById<EditText>(R.id.password_register).text.toString()

        when {
            username == "" -> {
                Toast.makeText(this@RegisterActivity, "please write username", Toast.LENGTH_LONG)
                    .show()
            }
            email == "" -> {
                Toast.makeText(this@RegisterActivity, "please write email", Toast.LENGTH_LONG)
                    .show()
            }
            password == "" -> {
                Toast.makeText(this@RegisterActivity, "please write password", Toast.LENGTH_LONG)
                    .show()
            }
            else -> {
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            firebaseUserID = mAuth.currentUser!!.uid
                            refUsers =
                                FirebaseDatabase.getInstance("https://heartratioapp-default-rtdb.europe-west1.firebasedatabase.app/").reference.child(
                                    "Users"
                                ).child(firebaseUserID)

                            val userHashMap = HashMap<String, Any>()
                            userHashMap["uid"] = firebaseUserID
                            userHashMap["username"] = username
                            userHashMap["ratio"] = "bad"


                            refUsers.updateChildren(userHashMap)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val intent =
                                            Intent(this@RegisterActivity, MainActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Error: " + task.exception!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }

    }


}