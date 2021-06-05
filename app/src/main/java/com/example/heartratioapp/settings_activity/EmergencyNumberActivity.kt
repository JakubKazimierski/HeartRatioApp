package com.example.heartratioapp.settings_activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.heartratioapp.R
import kotlinx.android.synthetic.main.activity_emergency_number.*

class EmergencyNumberActivity : AppCompatActivity(){

    private var emergencyNumber : String = "111"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_number)
        textView3.text = getString(R.string.current_number, emergencyNumber)
    }

    fun updateNumber(view: View) {
        emergencyNumber = editTextPhone.text.toString()
        textView3.text = getString(R.string.current_number, emergencyNumber)
    }
}
