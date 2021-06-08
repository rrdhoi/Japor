package com.bangkit.japorapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bangkit.japorapp.R
import com.bangkit.japorapp.data.response.ReportResponse
import com.bangkit.japorapp.databinding.FragmentHomeBinding
import com.bangkit.japorapp.ui.detail.DetailActivity
import com.bangkit.japorapp.ui.home.type.AllTypeFragment
import com.bangkit.japorapp.ui.home.type.FireTypeFragment
import com.bangkit.japorapp.ui.home.type.RoadTypeFragment
import com.bangkit.japorapp.ui.home.type.TreeTypeFragment
import com.bangkit.japorapp.utils.UserPreference
import com.bumptech.glide.Glide

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var userPrefs: UserPreference
    private var newReport: ReportResponse? = null

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

        observingValue()

        homeViewModel.getNewestReport(department)
        binding.cardNewestReport.setOnClickListener {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.KEY_REPORT, newReport)
            startActivity(intent)
        }

        setDefaultFragment()

        val colorBlue = ContextCompat.getColor(requireContext(), R.color.blue_700)
        val colorGrey = ContextCompat.getColor(requireContext(), R.color.grey_300)

        binding.tvAll.setOnClickListener {
            binding.tvAll.setTextColor(colorBlue)
            binding.tvRoad.setTextColor(colorGrey)
            binding.tvFire.setTextColor(colorGrey)
            binding.tvTree.setTextColor(colorGrey)

            setDefaultFragment()
        }
        binding.tvRoad.setOnClickListener {
            binding.tvRoad.setTextColor(colorBlue)
            binding.tvAll.setTextColor(colorGrey)
            binding.tvFire.setTextColor(colorGrey)
            binding.tvTree.setTextColor(colorGrey)

            setRoadFragment()
        }
        binding.tvFire.setOnClickListener {
            binding.tvFire.setTextColor(colorBlue)
            binding.tvRoad.setTextColor(colorGrey)
            binding.tvAll.setTextColor(colorGrey)
            binding.tvTree.setTextColor(colorGrey)

            setFireFragment()
        }
        binding.tvTree.setOnClickListener {
            binding.tvTree.setTextColor(colorBlue)
            binding.tvRoad.setTextColor(colorGrey)
            binding.tvFire.setTextColor(colorGrey)
            binding.tvAll.setTextColor(colorGrey)

            setTreeFragment()
        }
    }

    private fun observingValue() {
        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.pbHome.visibility = View.VISIBLE
            } else {
                binding.pbHome.visibility = View.GONE
            }
        }

        homeViewModel.newestReport.observe(viewLifecycleOwner) { newestReport ->
            newReport = newestReport

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
    }

    private fun setDefaultFragment() {
        Log.d("HomeFragment", "setFragment: all")

        val fragmentManager = childFragmentManager
        val reportTypeFragment = AllTypeFragment()

        fragmentManager.beginTransaction().apply {
            replace(R.id.type_report_container, reportTypeFragment, AllTypeFragment::class.java.simpleName)
            commit()
        }
    }

    private fun setTreeFragment() {
        Log.d("HomeFragment", "setFragment: tree")

        val fragmentManager = childFragmentManager
        val reportTypeFragment = TreeTypeFragment()

        fragmentManager.beginTransaction().apply {
            replace(R.id.type_report_container, reportTypeFragment, TreeTypeFragment::class.java.simpleName)
            commit()
        }
    }

    private fun setFireFragment() {
        Log.d("HomeFragment", "setFragment: fire")

        val fragmentManager = childFragmentManager
        val reportTypeFragment = FireTypeFragment()

        fragmentManager.beginTransaction().apply {
            replace(R.id.type_report_container, reportTypeFragment, FireTypeFragment::class.java.simpleName)
            commit()
        }
    }

    private fun setRoadFragment() {
        Log.d("HomeFragment", "setFragment: road")

        val fragmentManager = childFragmentManager
        val reportTypeFragment = RoadTypeFragment()

        fragmentManager.beginTransaction().apply {
            replace(R.id.type_report_container, reportTypeFragment, RoadTypeFragment::class.java.simpleName)
            commit()
        }
    }

}