package com.example.heartratioapp.settings_activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.heartratioapp.R

class SettingsActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        }

    fun changeEmergencyNumber(view: View) {
        val intent = Intent(this@SettingsActivity, EmergencyNumberActivity::class.java)
        startActivity(intent)
    }
    fun setNotifications(view: View) {
        val intent = Intent(this@SettingsActivity, NotificationsActivity::class.java)
        startActivity(intent)
    }

}