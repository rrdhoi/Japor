package com.bangkit.japorapp.ui.home

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
        fun bind(report: ReportResponse) {
            binding.tvTitle.text = report.judul
            binding.tvStatus.text = report.status

            val dateAndTime = report.tanggal
                .replace("T", " ")
                .replace("Z", "")
            binding.tvDateTime.text = dateAndTime

            when (binding.tvStatus.text) {
                "Valid" -> {
                    binding.tvStatus.setTextColor(Color.GREEN)
                }
                "Menunggu" -> {
                    binding.tvStatus.setTextColor(Color.rgb(230, 230, 0))
                }
                else -> {
                    binding.tvStatus.setTextColor(Color.RED)
                }
            }

            Glide.with(itemView.context)
                    .load(report.url)
                    .into(binding.ivEvent)

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