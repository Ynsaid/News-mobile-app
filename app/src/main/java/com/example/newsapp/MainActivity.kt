package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Declare Handler and Runnable as class members
    private val handler = Handler(Looper.getMainLooper())
    private val monitor: Runnable = object : Runnable {
        override fun run() {
            // Create an intent to navigate to HomePageActivity
            val intent = Intent(this@MainActivity, HomePageActivity::class.java)
            startActivity(intent)

            // Stop the Handler after starting the new activity to avoid infinite redirection
            handler.removeCallbacks(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        handler.postDelayed(monitor, 2000)
    }


    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(monitor)
    }
}