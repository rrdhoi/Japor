package com.bangkit.japorapp.ui.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.japorapp.data.network.ApiConfig
import com.bangkit.japorapp.data.response.ReportResponse
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ReportViewModel : ViewModel() {

    private var _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> get() = _isFinished

    private var _reportId = MutableLiveData<Long>()
    val reportId: LiveData<Long> get() = _reportId

    private var _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun sendReport(url: String) {
        val dateAndTime = "${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())}T${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())}Z"
        val title = "kosong"
        val desc = "kosong"
        val loc = "kosong"
        val status = "kosong"
        val urlProgress = "kosong"
        val category = "kosong"
        val uid = FirebaseAuth.getInstance().uid

        Log.d("ReportViewModel", dateAndTime)

        if (uid != null) {
            val client = ApiConfig.getApiService()
                .postOneReport(dateAndTime, title, desc, loc, status, url, urlProgress, uid, category)

            client.enqueue(object : Callback<ReportResponse> {
                override fun onResponse(
                    call: Call<ReportResponse>,
                    response: Response<ReportResponse>
                ) {
                    _reportId.value = response.body()?.id
                    _message.value = response.message()
                }

                override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                    _message.value = t.localizedMessage
                }
            })
        }
    }

    fun updateReport(id: Long, title: String, desc: String, category: String, loc: String) {
        val dateAndTime = "${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())}T${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())}Z"
        val status = "Menunggu"

        Log.d("ReportViewModel", dateAndTime)

        val client = ApiConfig.getApiService()
            .updateReport(id, dateAndTime, title, desc, loc, status, category)

        client.enqueue(object : Callback<ReportResponse> {
            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {
                if (response.isSuccessful) {
                    _isSuccess.value = true
                } else {
                    _isSuccess.value = false
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                _isSuccess.value = false
                _message.value = t.localizedMessage
            }
        })
    }

    fun cancelReport(reportId: Long) {
        val client = ApiConfig.getApiService().deleteOneReport(reportId)
        client.enqueue(object: Callback<ReportResponse> {
            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {
                _isFinished.value = true
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                _isFinished.value = false
            }
        })
    }

}