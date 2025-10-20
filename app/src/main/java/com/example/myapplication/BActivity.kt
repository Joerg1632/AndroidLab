package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class BActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)
        Log.d("Lifecycle", "BActivity onCreate")

        findViewById<Button>(R.id.buttonNextB).setOnClickListener {
            Log.d("Navigation", "Переход с B на C")
            startActivity(Intent(this, CActivity::class.java))
        }

        findViewById<Button>(R.id.buttonBackB).setOnClickListener {
            Log.d("Navigation", "Переход с B на A")
            finish()
        }
    }
}