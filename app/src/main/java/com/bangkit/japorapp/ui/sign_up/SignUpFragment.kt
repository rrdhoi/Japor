package com.bangkit.japorapp.ui.sign_up

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bangkit.japorapp.R
import com.bangkit.japorapp.databinding.FragmentSignupBinding
import com.bangkit.japorapp.ui.BaseView
import com.bangkit.japorapp.ui.HomeActivity
import com.bangkit.japorapp.utils.UserPreference
import java.lang.StringBuilder

class SignUpFragment : Fragment(), BaseView {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding as FragmentSignupBinding
    private val signUpViewModel: SignUpViewModel by viewModels()
    private var progressDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observingValue()
        clickingSignUp()
    }

    private fun observingValue() {
        signUpViewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                dismissLoading()

                Toast.makeText(
                    activity,
                    "Berhasil terdaftar!",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(activity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    .or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        signUpViewModel.message.observe(viewLifecycleOwner) { msg ->
            dismissLoading()

            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }

        signUpViewModel.user.observe(viewLifecycleOwner) { user ->
            val userPrefs = UserPreference(requireContext())
            userPrefs.setUser(user)
        }
    }

    private fun clickingSignUp() {
        binding.btnSignup.setOnClickListener {
            val firstName = binding.etFirstname.text.toString()
            val lastName = binding.etLastname.text.toString()
            val nik = binding.etNik.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmedPassword = binding.etConfirmPassword.text.toString()

            when {
                firstName == "" -> {
                    binding.etFirstname.error = "Tidak boleh kosong!"
                }
                lastName == "" -> {
                    binding.etLastname.error = "Tidak boleh kosong!"
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
                    showLoading()
                    val combineString = StringBuilder()
                    combineString.append(firstName).append(" ").append(lastName)
                    val fullName = combineString.toString()

                    signUpViewModel.signUp(email, confirmedPassword, fullName, nik)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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