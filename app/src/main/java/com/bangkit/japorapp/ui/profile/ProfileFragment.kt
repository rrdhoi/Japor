package com.bangkit.japorapp.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bangkit.japorapp.R
import com.bangkit.japorapp.databinding.FragmentProfileBinding
import com.bangkit.japorapp.ui.MainActivity
import com.bangkit.japorapp.ui.profile.delete_account.DeleteAccountFragment
import com.bangkit.japorapp.ui.profile.my_reports.MyReportsFragment
import com.bangkit.japorapp.utils.UserPreference
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding as FragmentProfileBinding

    private lateinit var userPreference: UserPreference
    private var selectedPhotoUri: Uri? = null
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreference = UserPreference(requireContext())
        if (userPreference.getUser().departemen != "User") {
            binding.tvMyListReport.visibility = View.INVISIBLE
            binding.view.visibility = View.INVISIBLE
        }

        observingValue()

        activity?.let {
            Glide.with(it)
                .load(userPreference.getUser().url)
                .into(binding.btnCivProfile)
        }
        binding.tvName.text = userPreference.getUser().nama
        binding.tvNik.text = userPreference.getUser().nik

        binding.btnCivProfile.setOnClickListener {
            openGalery()
        }

        binding.tvMyListReport.setOnClickListener {
            goToReportsFragment()
        }

        binding.tvDeleteAccount.setOnClickListener {
            goToDeleteAccountFragment()
        }

        binding.tvSignout.setOnClickListener {
            signOut()
        }
    }

    private fun observingValue() {
        profileViewModel.updatedUser.observe(viewLifecycleOwner) { updatedUser ->
            userPreference.setUser(updatedUser)
        }

        profileViewModel.isSuccessUpdate.observe(viewLifecycleOwner) { isSuccessUpdate ->
            if (isSuccessUpdate) {
                binding.pbFragmentProfile.visibility = View.GONE
                Toast.makeText(
                    activity,
                    "Foto profil berhasil diperbaharui!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        profileViewModel.message.observe(viewLifecycleOwner) { msg ->
            binding.pbFragmentProfile.visibility = View.GONE
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGalery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        @Suppress("DEPRECATION")
        startActivityForResult(intent, 0)
    }

    private fun goToReportsFragment() {
        val reportRecordsFragment = MyReportsFragment()
        val fragmentManager = parentFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(
                R.id.frame_container,
                reportRecordsFragment,
                MyReportsFragment::class.java.simpleName
            )
            addToBackStack(null)
            commit()
        }
    }

    private fun goToDeleteAccountFragment() {
        val deleteAccountFragment = DeleteAccountFragment()
        val fragmentManager = parentFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(
                R.id.frame_container,
                deleteAccountFragment,
                DeleteAccountFragment::class.java.simpleName
            )
            addToBackStack(null)
            commit()
        }
    }

    private fun signOut() {
        binding.pbFragmentProfile.visibility = View.VISIBLE

        val auth = FirebaseAuth.getInstance()
        auth.signOut()

        val userPrefs = UserPreference(requireContext())
        userPrefs.clearPrefs()

        binding.pbFragmentProfile.visibility = View.GONE

        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data

            activity?.let { Glide.with(it)
                .asBitmap()
                .load(selectedPhotoUri)
                .into(binding.btnCivProfile)
            }
            binding.pbFragmentProfile.visibility = View.VISIBLE

            uploadImage()
        }
    }

    private fun uploadImage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/profile-images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl
                    .addOnSuccessListener {
                        val idUser = userPreference.getUser().id
                        profileViewModel.updateUser(idUser, it.toString())
                    }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}