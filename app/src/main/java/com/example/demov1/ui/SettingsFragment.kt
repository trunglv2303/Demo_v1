package com.example.demov1.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.demov1.R
import androidx.appcompat.app.AppCompatDelegate
import com.example.demov1.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSettingsBinding.bind(view)
val prefs=requireContext().getSharedPreferences("setting", Context.MODE_PRIVATE)
        val isDrankMode=prefs.getBoolean("isDrankMode",false)
        binding.swichDrankMode.isChecked=isDrankMode
        binding.icBack.setOnClickListener  {
            parentFragmentManager.popBackStack()
        }
        binding.swichDrankMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("isDrankMode", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
