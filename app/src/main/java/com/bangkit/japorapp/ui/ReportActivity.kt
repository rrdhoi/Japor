package com.bangkit.japorapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.japorapp.databinding.ActivityReportBinding

class ReportActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_IMAGE_REQ_CODE = 103
    }

    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}