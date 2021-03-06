package com.bangkit.japorapp.ui.home.type

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.japorapp.data.network.ApiConfig
import com.bangkit.japorapp.data.response.ReportResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportTypeViewModel : ViewModel() {

    private val _report = MutableLiveData<List<ReportResponse>>()
    val report: LiveData<List<ReportResponse>> get() = _report

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun getReport(department: String, type: String) {
        Log.d("AllTypeFragment", "$department, $type")
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

                        if (size >= 0) {
                            when (department) {
                                "User" -> {
                                    when (type) {
                                        "All" -> {
                                            _report.value = response.body()
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
                                "Road" -> {
                                    lookForWaitingReport(size, reportType, "Jalan")
                                }
                                "Fire" -> {
                                    lookForWaitingReport(size, reportType, "Api")
                                }
                                "Tree" -> {
                                    lookForWaitingReport(size, reportType, "Pohon")
                                }
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
            if (reportType[i].kategori == category) {
                reportArray.add(reportType[i])
            }
        }
        _report.value = reportArray
    }

    private fun lookForWaitingReport(size: Int, reportType: List<ReportResponse>, category: String) {
        val reportArray = ArrayList<ReportResponse>()
        for (i in 0..size) {
            if (reportType[i].kategori == category && reportType[i].status == "Menunggu") {
                reportArray.add(reportType[i])
            }
        }
        _report.value = reportArray
    }

}