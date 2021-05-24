package com.bangkit.japorapp.ui.profile

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.japorapp.data.response.ReportResponse
import com.bangkit.japorapp.databinding.ListMyReportBinding
import com.bumptech.glide.Glide

class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.ListViewHolder>() {

    var reportList = ArrayList<ReportResponse>()
        set(report) {
            if (report.size > 0) {
                this.reportList.clear()
            }
            this.reportList.addAll(report)

            notifyDataSetChanged()
        }

    private lateinit var binding: ListMyReportBinding

    inner class ListViewHolder(binding: ListMyReportBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(report: ReportResponse) {
            binding.tvTitle.text = report.title
            binding.tvStatus.text = report.status

            when (binding.tvStatus.text) {
                "Done" -> {
                    binding.tvStatus.setTextColor(Color.GREEN)
                }
                "Pending" -> {
                    binding.tvStatus.setTextColor(Color.rgb(230, 230, 0))
                }
                else -> {
                    binding.tvStatus.setTextColor(Color.RED)
                }
            }

            Glide.with(itemView.context)
                    .load(report.photo)
                    .into(binding.ivEvent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = ListMyReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(reportList[position])
    }

    override fun getItemCount(): Int = reportList.size

}