package com.bangkit.japorapp.data.response

import com.google.gson.annotations.SerializedName

data class ReportResponse(
    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("nik")
    val nik: String = "",

    @field:SerializedName("place")
    val place: String = "",

    @field:SerializedName("date")
    val date: String = "",

    @field:SerializedName("time")
    val time: String = "",

    @field:SerializedName("title")
    val title: String = "",

    @field:SerializedName("photo")
    val photo: String = "",

    @field:SerializedName("desc")
    val desc: String = "",

    @field:SerializedName("type")
    val type: String = "",

    @field:SerializedName("status")
    val status: String = ""
)