package com.bangkit.japorapp.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.japorapp.R
import kotlinx.android.synthetic.main.activity_onboarding_four.*

class OnboardingFourActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_four)

        next_4.setOnClickListener {
            finishAffinity()

            val intent = Intent(this@OnboardingFourActivity,
                SignInActivity::class.java)
            startActivity(intent)
        }
    }
}