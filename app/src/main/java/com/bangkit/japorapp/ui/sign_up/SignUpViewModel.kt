package com.bangkit.japorapp.ui.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.japorapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpViewModel : ViewModel() {

    private var _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun signUp(email: String, confirmedPassword: String, fullName: String, nik: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, confirmedPassword)
                .addOnSuccessListener {
                    val uid = auth.uid
                    val user = User(fullName, nik, email)
                    val ref = FirebaseDatabase
                            .getInstance()
                            .getReference("/users/$uid")

                    ref.setValue(user)
                            .addOnSuccessListener {
                                _isSuccess.value = true
                            }
                            .addOnFailureListener {
                                _isSuccess.value = false
                                _message.value = it.localizedMessage
                            }
                }
                .addOnFailureListener {
                    _isSuccess.value = false
                    _message.value = it.localizedMessage
                }
    }

}