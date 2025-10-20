package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c)
        Log.d("Lifecycle", "CActivity onCreate")

        findViewById<Button>(R.id.buttonNextC).setOnClickListener {
            Log.d("Navigation", "Переход с C на A")
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<Button>(R.id.buttonBackC).setOnClickListener {
            Log.d("Lifecycle", "Переход с C на B")
            finish()
        }
    }
}