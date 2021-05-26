package com.bangkit.japorapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.japorapp.R
import com.bangkit.japorapp.data.response.ReportResponse
import com.bangkit.japorapp.databinding.FragmentHomeBinding
import com.bangkit.japorapp.ui.profile.ProfileFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding
    private lateinit var adapter: HomeAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observingValue()
        showRecyclerView()
    }

    private fun observingValue() {
        homeViewModel.report.observe(viewLifecycleOwner) { result ->
            adapter.reportList = result as ArrayList<ReportResponse>
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
        adapter = HomeAdapter()

        val recyclerView = binding.rvHome
        recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
    }

}