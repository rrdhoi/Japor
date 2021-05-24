package com.bangkit.japorapp.ui.sign_up

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bangkit.japorapp.databinding.FragmentSignupBinding
import com.bangkit.japorapp.ui.HomeActivity

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding as FragmentSignupBinding
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observingValue()
        clickingSignUp()
    }

    private fun observingValue() {
        signUpViewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                binding.progressBar.visibility = View.GONE

                Toast.makeText(activity,
                        "Berhasil terdaftar!",
                        Toast.LENGTH_SHORT).show()

                val intent = Intent(activity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        .or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        signUpViewModel.message.observe(viewLifecycleOwner) { msg ->
            binding.progressBar.visibility = View.GONE

            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun clickingSignUp() {
        binding.btnSignup.setOnClickListener {
            val fullName = binding.etFullname.text.toString()
            val nik = binding.etNik.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmedPassword = binding.etConfirmPassword.text.toString()

            when {
                fullName == "" -> {
                    binding.etFullname.error = "Tidak boleh kosong!"
                }
                nik == "" -> {
                    binding.etNik.error = "Tidak boleh kosong!"
                }
                nik.length != 16 -> {
                    binding.etNik.error = "Harus 16 digit!"
                }
                email == "" -> {
                    binding.etEmail.error = "Tidak boleh kosong!"
                }
                password == "" -> {
                    binding.etPassword.error = "Tidak boleh kosong!"
                }
                password.length < 6 -> {
                    binding.etPassword.error = "Minimal 6 karakter!"
                }
                confirmedPassword == "" -> {
                    binding.etConfirmPassword.error = "Tidak boleh kosong!"
                }
                password != confirmedPassword -> {
                    binding.etConfirmPassword.error = "Sandi tidak sama!"
                }
                else -> {
                    binding.progressBar.visibility = View.VISIBLE

                    signUpViewModel.signUp(email, confirmedPassword, fullName, nik)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}