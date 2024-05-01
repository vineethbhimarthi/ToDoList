package com.example.doit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.logging.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val i = Intent(this@SplashActivity, MainActivity::class.java)

        android.os.Handler().postDelayed({
            startActivity(i)
            finish()
        }, 1000)
    }
}

