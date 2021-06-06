package com.bangkit.japorapp.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.japorapp.data.network.ApiConfig
import com.bangkit.japorapp.data.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private var _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> get() = _user

    fun getOneUser(uid: String) {
        val client = ApiConfig.getApiService().getOneUser(uid)

        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    _user.value = response.body()
                    Log.d("SignInViewModel", "onSuccess: Success login!")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) = Unit
        })
    }

}