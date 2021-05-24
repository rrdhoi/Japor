package com.bangkit.japorapp.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.japorapp.databinding.ActivityOnboardingThreeBinding

class OnboardingThreeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingThreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingThreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.next3.setOnClickListener {
            val intent = Intent(this@OnboardingThreeActivity,
                OnboardingThreeActivity::class.java)
            startActivity(intent)
        }
    }

}