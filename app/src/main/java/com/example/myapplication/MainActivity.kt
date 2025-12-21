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

        Log.d("Lifecycle", "A onCreate")

        findViewById<Button>(R.id.buttonNextA).setOnClickListener {
            Log.d("Navigation", "A -> B")
            startActivity(Intent(this, BActivity::class.java))
        }
    }
}
