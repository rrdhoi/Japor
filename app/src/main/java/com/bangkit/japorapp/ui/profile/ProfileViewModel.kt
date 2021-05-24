package com.bangkit.japorapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.japorapp.data.network.ApiConfig
import com.bangkit.japorapp.data.response.ListReportResponse
import com.bangkit.japorapp.data.response.ReportResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    private val _report = MutableLiveData<List<ReportResponse>>()
    val report: LiveData<List<ReportResponse>> get() = _report

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun getReport(nik: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getMyReports(nik)
        client.enqueue(object : Callback<ListReportResponse> {
            override fun onResponse(
                call: Call<ListReportResponse>,
                response: Response<ListReportResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _report.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<ListReportResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.localizedMessage
            }
        })
    }

}