package com.bangkit.japorapp.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.japorapp.R
import kotlinx.android.synthetic.main.activity_onboarding_tree.*

class OnboardingTreeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_tree)

        next_3.setOnClickListener {
            val intent = Intent(this@OnboardingTreeActivity,
                OnboardingTreeActivity::class.java)
            startActivity(intent)
        }
    }
}