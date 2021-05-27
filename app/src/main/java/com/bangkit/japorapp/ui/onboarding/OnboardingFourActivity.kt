package com.bangkit.japorapp.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.japorapp.databinding.ActivityOnboardingFourBinding
import com.bangkit.japorapp.ui.MainActivity
import com.bangkit.japorapp.ui.sign_in.SignInFragment

class OnboardingFourActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingFourBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingFourBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.next4.setOnClickListener {
            finishAffinity()

            val intent = Intent(this@OnboardingFourActivity,
                MainActivity::class.java)
            startActivity(intent)
        }
    }
}