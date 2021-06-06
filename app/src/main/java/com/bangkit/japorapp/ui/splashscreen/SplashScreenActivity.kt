package com.bangkit.japorapp.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.japorapp.R
import com.bangkit.japorapp.ui.onboarding.OnboardingOneActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            val auth = Intent(this@SplashScreenActivity, OnboardingOneActivity::class.java)
            startActivity(auth)
            finish()
        }, 3000)
    }
}