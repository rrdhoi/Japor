package com.bangkit.japorapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReportResponse(
    @field:SerializedName("id")
    val id: Long = 0,

    @field:SerializedName("tanggal")
    val tanggal: String = "",

    @field:SerializedName("judul")
    val judul: String = "",

    @field:SerializedName("deskripsi")
    val deskripsi: String = "",

    @field:SerializedName("lokasi")
    val lokasi: String = "",

    @field:SerializedName("status")
    val status: String = "",

    @field:SerializedName("url")
    val url: String = "",

    @field:SerializedName("kategori")
    val kategori: String = "",

    @field:SerializedName("user_id")
    val user_id: String = ""
) : Parcelable