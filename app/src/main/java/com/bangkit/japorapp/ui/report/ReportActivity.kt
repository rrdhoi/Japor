package com.bangkit.japorapp.ui.report

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bangkit.japorapp.R
import com.bangkit.japorapp.databinding.ActivityReportBinding
import com.bangkit.japorapp.ui.HomeActivity
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class ReportActivity : AppCompatActivity(), CoroutineScope {

    companion object {
        const val CAMERA_IMAGE_REQ_CODE = 103
    }

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private lateinit var binding: ActivityReportBinding
    private val reportViewModel: ReportViewModel by viewModels()
    private var category = ""
    private var url = ""
    private var reportId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observingValue()

        val data = intent.getParcelableExtra<Uri>("URI")
        uploadImageToFirebaseStorage(data)

        Glide.with(this)
            .asBitmap()
            .load(data)
            .into(binding.imgReport)

        whenClickingRetakeButton()
        whenClickingCancelButton()
        whenClickingARecommendation()
        whenClickingSendButton()
    }

    private fun observingValue() {
        reportViewModel.reportId.observe(this) { id ->
            reportId = id
        }

        reportViewModel.isFinished.observe(this) { isFinished ->
            if (isFinished) {
                finish()
            }
        }

        reportViewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                binding.pbSendReport.visibility = View.GONE

                Toast.makeText(this,
                    "Laporan terkirim!",
                    Toast.LENGTH_SHORT).show()

                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    .or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        reportViewModel.message.observe(this) { msg ->
            binding.pbSendReport.visibility = View.GONE
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageToFirebaseStorage(uri: Uri?) {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/report-images/$filename")

        if (uri != null) {
            ref.putFile(uri)
                .addOnSuccessListener {
                    ref.downloadUrl
                        .addOnSuccessListener {
                            url = it.toString()
                            reportViewModel.sendReport(url)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@ReportActivity, "Gagal upload!", Toast.LENGTH_SHORT).show()
                        }
                }
        }
    }

    private fun whenClickingRetakeButton() {
        binding.btnRecapture.setOnClickListener {
            openCamera()
        }
    }

    private fun openCamera() = launch {
        withContext(Dispatchers.IO) {
            ImagePicker.with(this@ReportActivity)
                .cameraOnly()
                .compress(1024)
                .maxResultSize(620, 620)
                .start(CAMERA_IMAGE_REQ_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            when (requestCode) {
                CAMERA_IMAGE_REQ_CODE -> {
                    startActivity(Intent(this@ReportActivity, ReportActivity::class.java).putExtra("URI", uri))
                    reportViewModel.cancelReport(reportId)
                }
            }
        }
    }

    private fun whenClickingCancelButton() {
        binding.btnCancel.setOnClickListener {
            reportViewModel.cancelReport(reportId)
        }
    }

    private fun whenClickingARecommendation() {
        val colorBlue = ContextCompat.getColor(this, R.color.blue_500)
        val colorWhite = ContextCompat.getColor(this, R.color.white)

        binding.frameRecom1.setOnClickListener{
            binding.type1.setCardBackgroundColor(colorBlue)
            binding.type2.setCardBackgroundColor(colorWhite)
            binding.type3.setCardBackgroundColor(colorWhite)

            binding.imgRoad.setImageResource(R.drawable.img_road_blue)
            binding.imgFire.setImageResource(R.drawable.img_fire_white)
            binding.imgTree.setImageResource(R.drawable.img_tree_blue)

            category = "Api"
        }

        binding.frameRecom2.setOnClickListener {
            binding.type1.setCardBackgroundColor(colorWhite)
            binding.type2.setCardBackgroundColor(colorBlue)
            binding.type3.setCardBackgroundColor(colorWhite)

            binding.imgRoad.setImageResource(R.drawable.img_road_blue)
            binding.imgFire.setImageResource(R.drawable.img_fire_blue)
            binding.imgTree.setImageResource(R.drawable.img_tree_white)

            category = "Pohon"
        }

        binding.frameRecom3.setOnClickListener {
            binding.type1.setCardBackgroundColor(colorWhite)
            binding.type2.setCardBackgroundColor(colorWhite)
            binding.type3.setCardBackgroundColor(colorBlue)

            binding.imgRoad.setImageResource(R.drawable.img_road_white)
            binding.imgFire.setImageResource(R.drawable.img_fire_blue)
            binding.imgTree.setImageResource(R.drawable.img_tree_blue)

            category = "Jalan"
        }
    }

    private fun whenClickingSendButton() {
        binding.kirim.setOnClickListener {
            val loc = binding.inputLocEdt.text.toString()
            val title = binding.inputTitleEdt.text.toString()
            val desc = binding.edReview.text.toString()

            when {
                loc == "" -> {
                    Toast.makeText(this, "Lokasi masih kosong!", Toast.LENGTH_SHORT).show()
                }
                title == "" -> {
                    Toast.makeText(this, "Judul masih kosong!", Toast.LENGTH_SHORT).show()
                }
                desc == "" -> {
                    Toast.makeText(this, "Deskripsi masih kosong!", Toast.LENGTH_SHORT).show()
                }
                category == "" -> {
                    Toast.makeText(this, "Pilih kategori terlebih dahulu!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.pbSendReport.visibility = View.VISIBLE
                    reportViewModel.updateReport(reportId, title, desc, category, loc)
                }
            }
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Klik tombol 'Batalkan Laporan' untuk keluar!", Toast.LENGTH_SHORT).show()
    }

}