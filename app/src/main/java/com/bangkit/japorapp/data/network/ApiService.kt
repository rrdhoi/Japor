package com.bangkit.japorapp.data.network

import com.bangkit.japorapp.data.response.UserResponse
import com.bangkit.japorapp.data.response.ReportResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // Report
    @GET("api/lapor/")
    fun getAllReports() : Call<List<ReportResponse>>

    @FormUrlEncoded
    @POST("api/lapor/")
    fun postOneReport(
        @Field("tanggal") tanggal: String,
        @Field("judul") judul: String,
        @Field("deskripsi") deskripsi: String,
        @Field("lokasi") lokasi: String,
        @Field("status") status: String,
        @Field("url") url: String,
        @Field("url_progress") url_progress: String,
        @Field("user_id") user_id: String,
        @Field("kategori") kategori: String
    ) : Call<ReportResponse>

    @FormUrlEncoded
    @PATCH("api/lapor/{id}/")
    fun updateReport(
        @Path("id") id: Long,
        @Field("tanggal") tanggal: String,
        @Field("judul") judul: String,
        @Field("deskripsi") deskripsi: String,
        @Field("lokasi") lokasi: String,
        @Field("status") status: String,
        @Field("kategori") kategori: String
    ) : Call<ReportResponse>

    @DELETE("api/lapor/{id}/")
    fun deleteOneReport(
        @Path("id") id: Long
    ) : Call<ReportResponse>

    @FormUrlEncoded
    @PATCH("api/lapor/{id}/")
    fun validateReport(
        @Path("id") id: Long,
        @Field("status") status: String,
        @Field("url_progress") url_progress: String
    ) : Call<ReportResponse>

    // User
    @GET("api/user/{id}/")
    fun getOneUser(
        @Path("id") id: String
    ) : Call<UserResponse>

    @FormUrlEncoded
    @POST("api/user/")
    fun postOneUser(
        @Field("id") id: String,
        @Field("nama") nama: String,
        @Field("nik") nik: String,
        @Field("password") password: String,
        @Field("url") url: String,
        @Field("departemen") departemen: String
    ) : Call<UserResponse>

    @FormUrlEncoded
    @PATCH("api/user/{id}/")
    fun updateUserImgUrl(
        @Path("id") id: String,
        @Field("url") url: String
    ) : Call<UserResponse>

    @DELETE("api/user/{id}/")
    fun deleteOneUser(
        @Path("id") id: String
    ) : Call<UserResponse>

}