package com.bangkit.japorapp.ui.sign_in

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
import com.bangkit.japorapp.ui.forgot_password.ForgotPasswordFragment
import com.bangkit.japorapp.ui.sign_up.SignUpFragment
import com.bangkit.japorapp.ui.HomeActivity

class SignInFragment : Fragment() {

    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding as FragmentSigninBinding
    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observingValue()
        whenClickingForgotPassword()
        whenClickingSignIn()
        whenClickingSignUp()
    }

    private fun observingValue() {
        signInViewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            Log.d("SignInFragment", "observingValue: isSuccess is $isSuccess")

            if (isSuccess) {
                Toast.makeText(activity,
                        "Berhasil masuk!",
                        Toast.LENGTH_SHORT).show()

                val intent = Intent(activity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        .or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

                Log.d("SignInFragment", "whenClickingSignIn: Success login!")
            }
        }

        signInViewModel.message.observe(viewLifecycleOwner) { msg ->
            Log.d("SignInFragment", "observingValue: message is $msg")

            binding.progressBar.visibility = View.GONE

            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
            Log.d("SignInFragment", "whenClickingSignIn: Login failed! $msg")
        }
    }

    private fun whenClickingForgotPassword() {
        binding.tvForgotPassword.setOnClickListener {
            val forgotPasswordFragment = ForgotPasswordFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(
                        R.id.frame_container,
                        forgotPasswordFragment,
                        ForgotPasswordFragment::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun whenClickingSignIn() {
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
                    binding.progressBar.visibility = View.VISIBLE
                    signInViewModel.signIn(email, password)
                }
            }
        }
    }

    private fun whenClickingSignUp() {
        binding.btnGotoSignup.setOnClickListener {
            val signUpFragment = SignUpFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(
                        R.id.frame_container,
                        signUpFragment,
                        SignUpFragment::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}