package com.bangkit.japorapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.bangkit.japorapp.R
import com.bangkit.japorapp.databinding.ActivityMainBinding
import com.bangkit.japorapp.databinding.LayoutToolbarBinding
import com.bangkit.japorapp.ui.sign_in.SignInFragment.Companion.AUTH_FORGOT_PASSWORD
import com.bangkit.japorapp.ui.sign_in.SignInFragment.Companion.AUTH_SIGN_UP
import com.bangkit.japorapp.ui.sign_in.SignInFragment.Companion.PAGE_REQUEST_FORGOT_PASSWORD
import com.bangkit.japorapp.ui.sign_in.SignInFragment.Companion.PAGE_REQUEST_SIGN_UP


class MainActivity : AppCompatActivity() {

    private lateinit var bindingMain: ActivityMainBinding
    private lateinit var binding: LayoutToolbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        binding = bindingMain.toolbarMain
        setContentView(bindingMain.root)

        val pageRequestSignUp = intent.getIntExtra(PAGE_REQUEST_SIGN_UP, 0)
        val pageRequestForgotPassword = intent.getIntExtra(PAGE_REQUEST_FORGOT_PASSWORD, 0)

        if (pageRequestSignUp == AUTH_SIGN_UP) {
            toolbarSignUp()
            val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.signInFragment, true)
                    .build()
            Navigation.findNavController(findViewById(R.id.NavHostFragment))
                    .navigate(R.id.action_signInFragment_to_signUpFragment, null, navOptions)
        }

        if (pageRequestForgotPassword == AUTH_FORGOT_PASSWORD) {
            toolbarForgotPassword()
            val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.signInFragment, true)
                    .build()
            Navigation.findNavController(findViewById(R.id.NavHostFragment))
                    .navigate(R.id.action_signInFragment_to_forgotPasswordFragment, null, navOptions)
        }
//        openSignInFragment()
    }

    private fun toolbarSignUp() {
        binding.toolbar.title = "Buat Akun,"
        binding.toolbar.subtitle = "Daftar untuk memulai!"
        binding.toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_000, null)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun toolbarForgotPassword() {
        binding.toolbar.title = "Lupa sandi,"
        binding.toolbar.subtitle = "Buat sandi baru untuk memulai"
        binding.toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_000, null)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

//    private fun openSignInFragment() {
//        val fragmentManager = supportFragmentManager
//        val signInFragment = SignInFragment()
//        val fragment = fragmentManager.findFragmentByTag(SignInFragment::class.java.simpleName)
//
//        if (fragment !is SignInFragment) {
//            fragmentManager
//                .beginTransaction()
//                .add(R.id.frame_container, signInFragment, SignInFragment::class.java.simpleName)
//                .commit()
//        }
//    }

}