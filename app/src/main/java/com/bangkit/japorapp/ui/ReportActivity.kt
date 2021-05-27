package com.bangkit.japorapp.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bangkit.japorapp.R
import com.bangkit.japorapp.databinding.ActivityReportBinding
import com.bumptech.glide.Glide

class ReportActivity : AppCompatActivity(){

    companion object {
        const val CAMERA_IMAGE_REQ_CODE = 103
    }

    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val colorBlue = ContextCompat.getColor(this, R.color.blue_500)
        val colorWhite = ContextCompat.getColor(this, R.color.white)

        val data = intent.getParcelableExtra<Uri>("URI")

        Glide.with(this)
            .asBitmap()
            .load(data)
            .into(binding.imgReport)

        binding.frameRecom1.setOnClickListener{
            binding.type1.setCardBackgroundColor(colorBlue)
            binding.type2.setCardBackgroundColor(colorWhite)
            binding.type3.setCardBackgroundColor(colorWhite)

            binding.imgRoad.setImageResource(R.drawable.img_road_blue)
            binding.imgFire.setImageResource(R.drawable.img_fire_white)
            binding.imgTree.setImageResource(R.drawable.img_tree_blue)
        }

        binding.frameRecom2.setOnClickListener {
            binding.type1.setCardBackgroundColor(colorWhite)
            binding.type2.setCardBackgroundColor(colorBlue)
            binding.type3.setCardBackgroundColor(colorWhite)

            binding.imgRoad.setImageResource(R.drawable.img_road_blue)
            binding.imgFire.setImageResource(R.drawable.img_fire_blue)
            binding.imgTree.setImageResource(R.drawable.img_tree_white)
        }

        binding.frameRecom3.setOnClickListener {
            binding.type1.setCardBackgroundColor(colorWhite)
            binding.type2.setCardBackgroundColor(colorWhite)
            binding.type3.setCardBackgroundColor(colorBlue)

            binding.imgRoad.setImageResource(R.drawable.img_road_white)
            binding.imgFire.setImageResource(R.drawable.img_fire_blue)
            binding.imgTree.setImageResource(R.drawable.img_tree_blue)
        }
    }
}