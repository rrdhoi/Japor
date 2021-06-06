package com.bangkit.japorapp.ui.profile.delete_account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bangkit.japorapp.databinding.FragmentDeleteAccountBinding
import com.bangkit.japorapp.ui.MainActivity
import com.bangkit.japorapp.utils.UserPreference

class DeleteAccountFragment : Fragment() {

    private var _binding: FragmentDeleteAccountBinding? = null
    private val binding get() = _binding as FragmentDeleteAccountBinding

    private lateinit var userPref: UserPreference
    private val deleteAccountViewModel: DeleteAccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPref = UserPreference(requireContext())
        val userId = userPref.getUser().id

        observingValue()

        binding.btnDeleteAccount.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            when {
                email == "" -> {
                    Toast.makeText(activity, "Email masih kosong!", Toast.LENGTH_SHORT).show()
                }
                password == "" -> {
                    Toast.makeText(activity, "Password masih kosong!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.progressBar.visibility = View.VISIBLE
                    deleteAccountViewModel.deleteAccount(userId, email, password)
                }
            }
        }
    }

    private fun observingValue() {
        deleteAccountViewModel.isDeleted.observe(viewLifecycleOwner) { isDeleted ->
            if (isDeleted) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    activity,
                    "Hapus akun berhasil!",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent
                    .FLAG_ACTIVITY_CLEAR_TASK
                    .or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                activity?.finish()
            }
        }

        deleteAccountViewModel.message.observe(viewLifecycleOwner) { msg ->
            binding.progressBar.visibility = View.GONE
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }
    }

}