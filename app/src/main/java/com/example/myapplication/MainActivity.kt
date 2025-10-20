package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Lifecycle", "MainActivity onCreate")

        findViewById<Button>(R.id.buttonNextA).setOnClickListener {
            Log.d("Navigation", "Переход с A на B")
            val intent= Intent(this@MainActivity, BActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonExitA).setOnClickListener {
            Log.d("Lifecycle", "Finish app")
            finishAffinity()
        }
    }
}