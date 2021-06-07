package com.bangkit.japorapp.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.japorapp.R
import com.bangkit.japorapp.data.response.ReportResponse
import com.bangkit.japorapp.databinding.ListAllReportBinding
import com.bangkit.japorapp.ui.detail.DetailActivity
import com.bumptech.glide.Glide

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ListViewHolder>() {

    var reportList = ArrayList<ReportResponse>()
        set(report) {
            if (report.size > 0) {
                this.reportList.clear()
            }
            this.reportList.addAll(report)

            notifyDataSetChanged()
        }
    
    private lateinit var binding: ListAllReportBinding

    inner class ListViewHolder(val binding: ListAllReportBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(report: ReportResponse) {
            binding.tvTitle.text = report.judul
            binding.tvStatus.text = report.status

            val dateAndTime = report.tanggal
                .replace("T", " ")
                .replace("Z", "")
            binding.tvDateTime.text = dateAndTime

            when (binding.tvStatus.text) {
                "Valid" -> {
                    val drawableGreen = ContextCompat.getDrawable(itemView.context, R.drawable.shape_rectangle_green)
                    val colorGreen = ContextCompat.getColor(itemView.context, R.color.green_500)
                    binding.tvStatus.setTextColor(colorGreen)
                    binding.tvStatus.background = drawableGreen
                }
                "Menunggu" -> {
                    val drawableYellow = ContextCompat.getDrawable(itemView.context, R.drawable.shape_rectangle_yellow)
                    val colorYellow = ContextCompat.getColor(itemView.context, R.color.green_500)
                    binding.tvStatus.setTextColor(colorYellow)
                    binding.tvStatus.background = drawableYellow
                }
                else -> {
                    val drawableRed = ContextCompat.getDrawable(itemView.context, R.drawable.shape_rectangle_red)
                    val colorRed = ContextCompat.getColor(itemView.context, R.color.red_500)
                    binding.tvStatus.setTextColor(colorRed)
                    binding.tvStatus.background = drawableRed
                }
            }

            Glide.with(itemView.context)
                    .load(report.url)
                    .into(binding.ivEvent)

            Log.d("HomeAdapter", "bind: ${report.url}")

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.KEY_REPORT, report)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = ListAllReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(reportList[position])
    }

    override fun getItemCount(): Int = reportList.size

}