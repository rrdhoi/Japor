package com.bangkit.japorapp.data.network

import com.bangkit.japorapp.data.response.ListReportResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("exec?action=all-reports&sheetName=Laporan")
    fun getAllReports() : Call<ListReportResponse>

    @GET("exec?action=user-by-nik&sheetName=Laporan")
    fun getMyReports(@Query("nik") nik: String) : Call<ListReportResponse>

}