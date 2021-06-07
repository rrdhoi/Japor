package com.bangkit.japorapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.japorapp.data.response.ReportResponse
import com.bangkit.japorapp.databinding.FragmentHomeBinding
import com.bangkit.japorapp.utils.UserPreference
import com.bumptech.glide.Glide

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

    private lateinit var adapter: HomeAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var userPrefs: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPrefs = UserPreference(requireContext())
        val department = userPrefs.getUser().departemen

        if (department != "User") {
            binding.lyTypeReport.visibility = View.GONE
        }

        adapter = HomeAdapter()
        observingValue()
        homeViewModel.getReport(department)
        showRecyclerView()
    }

    private fun observingValue() {
        homeViewModel.report.observe(viewLifecycleOwner) { result ->
            adapter.reportList = result as ArrayList<ReportResponse>
        }

        homeViewModel.newestReport.observe(viewLifecycleOwner) { newestReport ->
            activity?.let {
                Glide.with(it)
                    .load(newestReport.url)
                    .into(binding.ivEvent)
            }
            binding.tvTitle.text = newestReport.judul
            binding.tvStatus.text = newestReport.status

            val dateAndTime = newestReport.tanggal
                .replace("T", " ")
                .replace("Z", "")
            binding.tvDateTime.text = dateAndTime
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.pbHome.visibility = View.VISIBLE
            } else {
                binding.pbHome.visibility = View.GONE
            }
        }

        homeViewModel.message.observe(viewLifecycleOwner) { message ->
            binding.tvMessage.text = message
        }
    }

    private fun showRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(activity)

        if (userPrefs.getUser().departemen == "User") {
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.stackFromEnd = true
        }

        binding.rvHome.layoutManager = linearLayoutManager
        binding.rvHome.setHasFixedSize(true)
        binding.rvHome.adapter = adapter
    }

}