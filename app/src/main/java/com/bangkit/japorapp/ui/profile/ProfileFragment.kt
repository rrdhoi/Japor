package com.bangkit.japorapp.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.japorapp.R
import com.bangkit.japorapp.databinding.FragmentProfileBinding
import com.bangkit.japorapp.utils.UserPreference

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding as FragmentProfileBinding

    private lateinit var userPreference: UserPreference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreference = UserPreference(requireContext())

        binding.tvName.text = userPreference.getUser().fullName
        binding.tvNik.text = userPreference.getUser().nik

        binding.tvMyListReport.setOnClickListener {
            val reportRecordsFragment = ReportRecordsFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(
                    R.id.frame_container,
                    reportRecordsFragment,
                    ReportRecordsFragment::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
        }
    }
}