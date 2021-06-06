package com.bangkit.japorapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.japorapp.data.network.ApiConfig
import com.bangkit.japorapp.data.response.ReportResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _report = MutableLiveData<List<ReportResponse>>()
    val report: LiveData<List<ReportResponse>> get() = _report

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun getReport(department: String) {
        _isLoading.value = true
        val reportArray = ArrayList<ReportResponse>()
        val client = ApiConfig.getApiService().getAllReports()

        client.enqueue(object : Callback<List<ReportResponse>> {
            override fun onResponse(
                call: Call<List<ReportResponse>>,
                response: Response<List<ReportResponse>>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {

                    val reportType = response.body()
                    if (reportType != null) {
                        val size = reportType.size - 1

                        when (department) {
                            "User" -> {
                                _report.value = response.body()
                            }
                            "Road" -> {
                                for (i in 0..size) {
                                    if (reportType[i].kategori == "Jalan" && reportType[i].status == "Menunggu") {
                                        reportArray.add(reportType[i])
                                    }
                                }
                                _report.value = reportArray
                            }
                            "Fire" -> {
                                for (i in 0..size) {
                                    if (reportType[i].kategori == "Api" && reportType[i].status == "Menunggu") {
                                        reportArray.add(reportType[i])
                                    }
                                }
                                _report.value = reportArray
                            }
                            "Tree" -> {
                                for (i in 0..size) {
                                    if (reportType[i].kategori == "Pohon" && reportType[i].status == "Menunggu") {
                                        reportArray.add(reportType[i])
                                    }
                                }
                                _report.value = reportArray
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<ReportResponse>>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.localizedMessage
            }
        })
    }

}