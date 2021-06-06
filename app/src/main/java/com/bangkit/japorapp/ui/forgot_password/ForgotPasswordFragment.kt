package com.bangkit.japorapp.ui.forgot_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bangkit.japorapp.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding as FragmentForgotPasswordBinding
    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observingValue()
        clickingForgotPassword()
    }

    private fun observingValue() {
        forgotPasswordViewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                binding.progressBar.visibility = View.GONE

                Toast.makeText(activity, "Cek email Anda!", Toast.LENGTH_SHORT).show()

                binding.tvCheckEmail.visibility = View.VISIBLE
            }
        }


        forgotPasswordViewModel.message.observe(viewLifecycleOwner) { msg ->
            binding.progressBar.visibility = View.GONE

            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }

    }


    private fun clickingForgotPassword() {
        binding.btnSendVerificationEmail.setOnClickListener {
            val email = binding.etEmail.text.toString()

            if (email == "") {
                binding.etEmail.error = "Email masih kosong!"
            } else {
                binding.progressBar.visibility = View.VISIBLE

                forgotPasswordViewModel.forgotPassword(email)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}