package com.example.demov1.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.demov1.R
import com.example.demov1.data.DataManager
import com.example.demov1.databinding.HomefragmentBinding
import java.text.DateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment(R.layout.homefragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = HomefragmentBinding.bind(view)
        binding.totalincom.text = "${DataManager.sumIncome()} ₫"
        binding.totalexpresive.text = "${DataManager.sumExpense()} ₫"
        binding.totalMoney.text = "${DataManager.totalMoney()} ₫"
        val dateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.forLanguageTag("vi-VN"))
        binding.today.text = dateFormat.format(Date())
    }
}
