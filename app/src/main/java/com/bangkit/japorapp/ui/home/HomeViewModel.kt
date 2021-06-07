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

    private val _newestReport = MutableLiveData<ReportResponse>()
    val newestReport: LiveData<ReportResponse> get() = _newestReport

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun getReport(department: String) {
        _isLoading.value = true
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
                                _newestReport.value = reportType[size]
                            }
                            "Road" -> {
                                lookForReport(size, reportType, "Jalan")
                            }
                            "Fire" -> {
                                lookForReport(size, reportType, "Api")
                            }
                            "Tree" -> {
                                lookForReport(size, reportType, "Pohon")
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

    private fun lookForReport(size: Int, reportType: List<ReportResponse>, category: String) {
        val reportArray = ArrayList<ReportResponse>()
        for (i in 0..size) {
            if (reportType[i].kategori == category && reportType[i].status == "Menunggu") {
                reportArray.add(reportType[i])
            }
        }
        _report.value = reportArray
        val arraySize = reportArray.size - 1
        if (arraySize >= 0) {
            _newestReport.value = reportArray[arraySize]
        }
    }

}