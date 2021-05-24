package com.bangkit.japorapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.japorapp.R
import com.bangkit.japorapp.databinding.ActivityMainBinding
import com.bangkit.japorapp.ui.sign_in.SignInFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openSignInFragment()
    }

    private fun openSignInFragment() {
        val fragmentManager = supportFragmentManager
        val signInFragment = SignInFragment()
        val fragment = fragmentManager.findFragmentByTag(SignInFragment::class.java.simpleName)

        if (fragment !is SignInFragment) {
            fragmentManager
                .beginTransaction()
                .add(R.id.frame_container, signInFragment, SignInFragment::class.java.simpleName)
                .commit()
        }
    }

}