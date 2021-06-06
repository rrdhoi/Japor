package com.bangkit.japorapp.ui.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.japorapp.data.network.ApiConfig
import com.bangkit.japorapp.data.response.UserResponse
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    private var _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> get() = _user

    private var _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun signUp(email: String, confirmedPassword: String, nama: String, nik: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, confirmedPassword)
            .addOnSuccessListener {
                val uid = auth.uid
                val url =
                    "https://firebasestorage.googleapis.com/v0/b/japor-45077.appspot.com/o/profile.png?alt=media&token=5414e4b1-6a81-4990-8f02-4c695bb703b6"
                val departemen = "User"

                if (uid != null) {
                    val client = ApiConfig.getApiService()
                        .postOneUser(uid, nama, nik, confirmedPassword, url, departemen)

                    client.enqueue(object : Callback<UserResponse> {
                        override fun onResponse(
                            call: Call<UserResponse>,
                            response: Response<UserResponse>
                        ) {
                            if (response.isSuccessful) {
                                _isSuccess.value = true
                                _user.value = response.body()
                            } else {
                                _isSuccess.value = false
                                _message.value = response.message()
                            }
                        }

                        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                            _isSuccess.value = false
                            _message.value = t.localizedMessage
                        }
                    })
                }
            }
            .addOnFailureListener {
                _isSuccess.value = false
                _message.value = it.localizedMessage
            }
    }

}