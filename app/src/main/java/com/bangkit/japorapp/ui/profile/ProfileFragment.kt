package com.bangkit.japorapp.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.japorapp.data.response.ReportResponse
import com.bangkit.japorapp.databinding.FragmentProfileBinding
import com.bangkit.japorapp.utils.UserPreference

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding as FragmentProfileBinding
    private lateinit var adapter: ProfileAdapter
    private val profileViewModel: ProfileViewModel by viewModels()

    private lateinit var userPreference: UserPreference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreference = UserPreference(requireContext())
        val nik = userPreference.getUser().nik

        binding.tvName.text = userPreference.getUser().fullName
        binding.tvEmail.text = userPreference.getUser().email
        binding.tvNik.text = userPreference.getUser().nik

        profileViewModel.getReport(nik)

        observingValue()
        showRecyclerView()
    }

    private fun observingValue() {
        profileViewModel.report.observe(viewLifecycleOwner) { result ->
            adapter.reportList = result as ArrayList<ReportResponse>
        }
        profileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.pbHome.visibility = View.VISIBLE
            } else {
                binding.pbHome.visibility = View.GONE
            }
        }
        profileViewModel.message.observe(viewLifecycleOwner) { message ->
            binding.tvMessage.text = message
        }
    }

    private fun showRecyclerView() {
        adapter = ProfileAdapter()
        binding.rvProfile.adapter = adapter
        binding.rvProfile.layoutManager = LinearLayoutManager(activity)
    }

}