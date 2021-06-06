package com.bangkit.japorapp.ui.sign_in

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.japorapp.data.network.ApiConfig
import com.bangkit.japorapp.data.response.UserResponse
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel : ViewModel() {

    private var _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> get() = _user

    private var _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun signIn(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid = auth.uid

                if (uid != null) {
                    val client = ApiConfig.getApiService().getOneUser(uid)

                    client.enqueue(object : Callback<UserResponse> {
                        override fun onResponse(
                            call: Call<UserResponse>,
                            response: Response<UserResponse>
                        ) {
                            if (response.isSuccessful) {
                                _user.value = response.body()
                                _isSuccess.value = true
                                Log.d("SignInViewModel", "onSuccess: Success login!")
                            } else {
                                _message.value = response.message()
                                _isSuccess.value = false
                            }
                        }

                        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                            _message.value = t.localizedMessage
                            _isSuccess.value = false
                        }
                    })
                }
            }
            .addOnFailureListener {
                _isSuccess.value = false
                _message.postValue(it.localizedMessage)
                Log.d("SignInViewModel", "onFailure: Login failed! ${_message.value}")
            }
    }

}