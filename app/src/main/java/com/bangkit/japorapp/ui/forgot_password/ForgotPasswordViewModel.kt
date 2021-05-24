package com.bangkit.japorapp.ui.forgot_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {

    private var _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun forgotPassword(email: String) {
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    _isSuccess.value = true
                }
                .addOnFailureListener {
                    _isSuccess.value = false
                    _message.value = it.localizedMessage
                }
    }

}