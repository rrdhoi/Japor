package com.bangkit.japorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ReportActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_IMAGE_REQ_CODE = 103
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
    }
}