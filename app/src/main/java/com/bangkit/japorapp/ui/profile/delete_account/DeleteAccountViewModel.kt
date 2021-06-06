package com.bangkit.japorapp.ui.profile.delete_account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.japorapp.data.network.ApiConfig
import com.bangkit.japorapp.data.response.UserResponse
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteAccountViewModel : ViewModel() {

    private var _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> get() = _isDeleted

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun deleteAccount(userId: String, email: String, password: String) {
        val client = ApiConfig.getApiService().deleteOneUser(userId)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val credential = EmailAuthProvider.getCredential(email, password)

                    user?.reauthenticate(credential)
                        ?.addOnSuccessListener {
                            user.delete()
                                .addOnSuccessListener {
                                    _isDeleted.value = true
                                }
                        }
                } else {
                    _isDeleted.value = false
                    _message.value = "Gagal merespon!"
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isDeleted.value = false
                _message.value = t.localizedMessage
            }
        })
    }

}