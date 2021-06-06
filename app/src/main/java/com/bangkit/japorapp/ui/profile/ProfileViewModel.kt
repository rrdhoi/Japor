package com.bangkit.japorapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.japorapp.data.network.ApiConfig
import com.bangkit.japorapp.data.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    private var _isSuccessUpdate = MutableLiveData<Boolean>()
    val isSuccessUpdate: LiveData<Boolean> get() = _isSuccessUpdate

    private var _updatedUser = MutableLiveData<UserResponse>()
    val updatedUser: LiveData<UserResponse> get() = _updatedUser

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun updateUser(id: String, url: String) {
        val client = ApiConfig.getApiService().updateUserImgUrl(id, url)

        client.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    _updatedUser.value = response.body()
                    _isSuccessUpdate.value = true
                } else {
                    _isSuccessUpdate.value = false
                    _message.value = "Gagal merespon!"
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isSuccessUpdate.value = false
                _message.value = t.localizedMessage
            }
        })
    }

}