package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class BActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)

        findViewById<Button>(R.id.buttonNextB).setOnClickListener {
            startActivity(Intent(this, CActivity::class.java))
        }

        findViewById<Button>(R.id.buttonBackB).setOnClickListener {
            finish()
        }
    }
}