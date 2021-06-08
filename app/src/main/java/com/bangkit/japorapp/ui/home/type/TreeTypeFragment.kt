package com.bangkit.japorapp.ui.home.type

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.japorapp.data.response.ReportResponse
import com.bangkit.japorapp.databinding.FragmentReportTypeBinding
import com.bangkit.japorapp.ui.home.HomeAdapter
import com.bangkit.japorapp.utils.UserPreference

class TreeTypeFragment : Fragment() {

    private var _binding: FragmentReportTypeBinding? = null
    private val binding get() = _binding as FragmentReportTypeBinding

    private lateinit var adapter: HomeAdapter
    private val reportTypeViewModel: ReportTypeViewModel by viewModels()
    private lateinit var userPrefs: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ReportTypeFragment",  "type: tree")

        userPrefs = UserPreference(requireContext())
        val department = userPrefs.getUser().departemen

        adapter = HomeAdapter()
        observingValue()
        reportTypeViewModel.getReport(department, "Tree")
        showRecyclerView()
    }

    private fun observingValue() {
        reportTypeViewModel.report.observe(viewLifecycleOwner) { result ->
            adapter.reportList = result as ArrayList<ReportResponse>
        }

        reportTypeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.pbReportType.visibility = View.VISIBLE
            } else {
                binding.pbReportType.visibility = View.GONE
            }
        }

        reportTypeViewModel.message.observe(viewLifecycleOwner) { message ->
            binding.tvMessageReportType.text = message
        }
    }

    private fun showRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(activity)

        if (userPrefs.getUser().departemen == "User") {
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.stackFromEnd = true
        }

        binding.rvReportType.layoutManager = linearLayoutManager
        binding.rvReportType.setHasFixedSize(true)
        binding.rvReportType.adapter = adapter
    }

}