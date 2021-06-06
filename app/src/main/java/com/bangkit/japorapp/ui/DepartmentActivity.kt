package com.bangkit.japorapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bangkit.japorapp.R
import com.bangkit.japorapp.databinding.ActivityDepartmentBinding

class DepartmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDepartmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDepartmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = Navigation.findNavController(this, R.id.department_fragment)
        NavigationUI.setupWithNavController(binding.departmentBottomNav, navController)
    }

}