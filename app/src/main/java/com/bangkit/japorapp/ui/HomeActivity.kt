package com.bangkit.japorapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bangkit.japorapp.R
import com.bangkit.japorapp.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.bangkit.japorapp.model.User
import com.bangkit.japorapp.utils.UserPreference
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HomeActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private lateinit var binding: ActivityHomeBinding
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isLogin()

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(1).isEnabled = false

        binding.floatingButtonCamera.setOnClickListener {
            openCamera()
        }

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        /* Code for sign out
        binding.btnSignout.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()

            val userPrefs = UserPreference(this@HomeActivity)
            userPrefs.clearPrefs()

            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        */
    }

    private fun isLogin() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            saveToSharedPref(uid)
        }
    }

    private fun saveToSharedPref(uid: String) {
        Log.d("HomeActivity", "saveToSharedPref with uid: $uid")

        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("HomeActivity", "Error message: ${error.message}")
            }
            // Masalah disini
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)

                val userPrefs = UserPreference(this@HomeActivity)
                currentUser?.let { userPrefs.setUser(it) }

                Log.d("HomeActivity", "Current User: ${currentUser?.fullName}")
            }
        })
    }

    private fun openCamera() = launch {
        withContext(Dispatchers.IO) {
            ImagePicker.with(this@HomeActivity)
                .cameraOnly()
                .compress(1024)
                .maxResultSize(620, 620)
                .start(ReportActivity.CAMERA_IMAGE_REQ_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            when (requestCode) {
                ReportActivity.CAMERA_IMAGE_REQ_CODE -> {
                    startActivity(Intent(this@HomeActivity, ReportActivity::class.java).putExtra("URI", uri))
                }
            }
        }
    }

}