package com.bangkit.japorapp.data.response

import com.google.gson.annotations.SerializedName

data class ListReportResponse(
    @field:SerializedName("results")
    val results: List<ReportResponse>
)