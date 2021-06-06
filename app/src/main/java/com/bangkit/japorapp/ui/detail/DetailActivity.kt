package com.bangkit.japorapp.ui.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bangkit.japorapp.data.network.ApiConfig
import com.bangkit.japorapp.data.response.ReportResponse
import com.bangkit.japorapp.databinding.ActivityDetailBinding
import com.bangkit.japorapp.ui.report.ReportActivity
import com.bangkit.japorapp.utils.UserPreference
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.coroutines.CoroutineContext

class DetailActivity : AppCompatActivity(), CoroutineScope {

    companion object {
        const val KEY_REPORT = "key_report"
    }

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var userPref: UserPreference
    private var uri: Uri? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPref = UserPreference(this)
        if (userPref.getUser().departemen == "User") {
            binding.clDepartment.visibility = View.GONE
        }

        val report = intent.getParcelableExtra<ReportResponse>(KEY_REPORT)

        if (report != null) {
            report.user_id.let { detailViewModel.getOneUser(it) }
            observingValue()

            Glide.with(this)
                .load(report.url)
                .into(binding.imgReport)

            val dateAndTime = report.tanggal
                .replace("T", " pukul ")
                .replace("Z", "")

            binding.tvDateTimeLoc.text = "$dateAndTime di ${report.lokasi}"
            binding.tvTitleDetail.text = report.judul
            binding.tvDescDetail.text = report.deskripsi

            whenClickingButton(report.id)
            setCategoryState(report)
        }

    }

    private fun whenClickingButton(reportId: Long) {
        binding.ibDepartment.setOnClickListener {
            openCamera()
        }

        binding.btnValid.setOnClickListener {
            if (uri != null) {
                binding.pbDetailDepartment.visibility = View.VISIBLE
                uploadImageThenValidateReport(reportId, "Valid")
            } else {
                Toast.makeText(this, "Foto masih kosong!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnInvalid.setOnClickListener {
            if (uri != null) {
                binding.pbDetailDepartment.visibility = View.VISIBLE
                uploadImageThenValidateReport(reportId, "Tidak Valid")
            } else {
                Toast.makeText(this, "Foto masih kosong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() = launch {
        withContext(Dispatchers.IO) {
            ImagePicker.with(this@DetailActivity)
                .cameraOnly()
                .compress(1024)
                .maxResultSize(620, 620)
                .start(ReportActivity.CAMERA_IMAGE_REQ_CODE)
        }
    }

    private fun uploadImageThenValidateReport(reportId: Long, status: String) {
        if (uri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/validation-images/$filename")

        ref.putFile(uri!!)
            .addOnSuccessListener {
                ref.downloadUrl
                    .addOnSuccessListener {
                        validateOrInvalidateReport(reportId, status, it.toString())
                    }
            }
    }

    private fun validateOrInvalidateReport(reportId: Long, status: String, urlPogress: String) {
        val client = ApiConfig.getApiService().validateReport(reportId, status, urlPogress)
        client.enqueue(object: Callback<ReportResponse> {
            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {
                if (response.isSuccessful) {
                    binding.pbDetailDepartment.visibility = View.GONE
                    Toast.makeText(this@DetailActivity, "Validasi sukses!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                binding.pbDetailDepartment.visibility = View.GONE
                Toast.makeText(this@DetailActivity, "Gagal memvalidasi!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setCategoryState(report: ReportResponse?) {
        when (report?.kategori) {
            "Api" -> {
                binding.frameRecom2.visibility = View.GONE
                binding.frameRecom3.visibility = View.GONE

                binding.kategoriDetail2.visibility = View.GONE
                binding.kategoriDetail3.visibility = View.GONE
            }
            "Pohon" -> {
                binding.frameRecom1.visibility = View.GONE
                binding.frameRecom3.visibility = View.GONE

                binding.kategoriDetail1.visibility = View.GONE
                binding.kategoriDetail3.visibility = View.GONE
            }
            "Jalan" -> {
                binding.frameRecom1.visibility = View.GONE
                binding.frameRecom2.visibility = View.GONE

                binding.kategoriDetail1.visibility = View.GONE
                binding.kategoriDetail2.visibility = View.GONE
            }
        }
    }

    private fun observingValue() {
        detailViewModel.user.observe(this) { user ->
            Glide.with(this)
                .load(user.url)
                .into(binding.civPpDetail)
            binding.tvNameDetail.text = user.nama
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            uri = data?.data

            Glide.with(this)
                .asBitmap()
                .load(uri)
                .into(binding.ivCapturedDepartment)

            binding.ibDepartment.alpha = 0f
        }
    }

}