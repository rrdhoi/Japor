package com.bangkit.japorapp.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.japorapp.databinding.ActivityOnboardingOneBinding
import com.bangkit.japorapp.ui.HomeActivity
import com.bangkit.japorapp.ui.MainActivity
import com.bangkit.japorapp.utils.Preferences
import com.google.firebase.auth.FirebaseAuth

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

            val uid = FirebaseAuth.getInstance().uid

            if (uid != null) {
                val intent = Intent(this@OnboardingOneActivity,
                    HomeActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@OnboardingOneActivity,
                    MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.next1.setOnClickListener {
            val intent = Intent(this@OnboardingOneActivity,
                OnboardingTwoActivity::class.java)
            startActivity(intent)
        }

        binding.btnDaftar.setOnClickListener {
            preferences.setValues("onboarding", "1")
            finishAffinity()

            val intent = Intent(this@OnboardingOneActivity,
                MainActivity::class.java)
            startActivity(intent)
        }
    }
}