package com.bangkit.japorapp.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.japorapp.databinding.ActivityOnboardingOneBinding
import com.bangkit.japorapp.utils.Preferences

class OnboardingOneActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences
    private lateinit var binding: ActivityOnboardingOneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = Preferences(this)

        if (preferences.getValues("onboarding").equals("1")) {
            finishAffinity()

//            val intent = Intent(this@OnboardingOneActivity,
//                SignInActivity::class.java)
//            startActivity(intent)
        }

        binding.next1.setOnClickListener {
            val intent = Intent(this@OnboardingOneActivity,
                OnboardingTwoActivity::class.java)
            startActivity(intent)
        }

        binding.btnDaftar.setOnClickListener {
            preferences.setValues("onboarding", "1")
            finishAffinity()

//            val intent = Intent(this@OnboardingOneActivity,
//                SignInActivity::class.java)
//            startActivity(intent)
        }
    }
}