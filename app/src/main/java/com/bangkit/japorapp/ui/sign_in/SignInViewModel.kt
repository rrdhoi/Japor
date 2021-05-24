package com.bangkit.japorapp.ui.sign_in

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel : ViewModel() {

    private var _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun signIn(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _isSuccess.value = true
                Log.d("SignInViewModel", "onSuccess: Success login!")
            }
            .addOnFailureListener {
                _isSuccess.value = false
                _message.postValue(it.localizedMessage)
                Log.d("SignInViewModel", "onFailure: Login failed! ${_message.value}")
            }
    }

}