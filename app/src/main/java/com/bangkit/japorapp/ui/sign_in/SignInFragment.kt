package com.bangkit.japorapp.ui.sign_in

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bangkit.japorapp.R
import com.bangkit.japorapp.databinding.FragmentSigninBinding
import com.bangkit.japorapp.ui.BaseView
import com.bangkit.japorapp.ui.DepartmentActivity
import com.bangkit.japorapp.ui.HomeActivity
import com.bangkit.japorapp.ui.MainActivity
import com.bangkit.japorapp.utils.UserPreference

class SignInFragment : Fragment(), BaseView {

    companion object {
        const val AUTH_SIGN_UP = 1
        const val AUTH_FORGOT_PASSWORD = 2
        const val PAGE_REQUEST_SIGN_UP = "page_request_sign_up"
        const val PAGE_REQUEST_FORGOT_PASSWORD = "page_request_forgot_password"
    }

    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding as FragmentSigninBinding
    private val signInViewModel: SignInViewModel by viewModels()
    private var department = ""
    private var progressDialog: Dialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observingValue()
        whenClickingButton()
    }

    private fun observingValue() {
        signInViewModel.user.observe(viewLifecycleOwner) { user ->
            department = user.departemen

            val userPrefs = UserPreference(requireContext())
            userPrefs.setUser(user)
        }

        signInViewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            Log.d("SignInFragment", "observingValue: isSuccess is $isSuccess")

            if (isSuccess) {
                Toast.makeText(activity,
                    "Berhasil masuk!",
                    Toast.LENGTH_SHORT).show()

                if (department == "User") {
                    val intent = Intent(activity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        .or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    val intent = Intent(activity, DepartmentActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        .or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }

                Log.d("SignInFragment", "whenClickingSignIn: Success login!")
            }
        }

        signInViewModel.message.observe(viewLifecycleOwner) { msg ->
            Log.d("SignInFragment", "observingValue: message is $msg")

            dismissLoading()

            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
            Log.d("SignInFragment", "whenClickingSignIn: Login failed! $msg")
        }
    }

    private fun whenClickingButton() {
        binding.btnSignin.setOnClickListener {
            Log.d("SignInFragment", "whenClickingSignIn")

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            when {
                email == "" -> {
                    binding.etEmail.error = "Tidak boleh kosong!"
                }
                password == "" -> {
                    binding.etPassword.error = "Tidak boleh kosong!"
                }
                else -> {
                    showLoading()
                    signInViewModel.signIn(email, password)
                }
            }
        }

        binding.btnGotoSignup.setOnClickListener {
            val signup = Intent(activity, MainActivity::class.java)
            signup.putExtra(PAGE_REQUEST_SIGN_UP, AUTH_SIGN_UP)
            startActivity(signup)
        }

        binding.tvForgotPassword.setOnClickListener {
            val forgotPassword = Intent(activity, MainActivity::class.java)
            forgotPassword.putExtra(PAGE_REQUEST_FORGOT_PASSWORD, AUTH_FORGOT_PASSWORD)
            startActivity(forgotPassword)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("InflateParams")
    private fun initView() {
        progressDialog = Dialog(requireContext())
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_loader, null)

        progressDialog?.let {
            it.setContentView(dialogLayout)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.dismiss()
    }

}