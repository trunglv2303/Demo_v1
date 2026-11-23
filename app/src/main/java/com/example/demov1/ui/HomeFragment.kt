package com.example.demov1.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demov1.R
import com.example.demov1.adapter.TransactionAdapter
import com.example.demov1.data.DataManager
import com.example.demov1.databinding.HomefragmentBinding
import com.example.demov1.data.remote.RetrofitClient
import kotlinx.coroutines.launch
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
        val todayDate =
            java.text.SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("vi-VN")).format(Date())
        binding.todaytransaction.text = "Hôm nay • $todayDate"

        val todayTransactions = DataManager.sampleTransactions.filter { it.date.trim() == todayDate }

        if (todayTransactions.isEmpty()) {
            binding.todaytransaction.visibility = View.VISIBLE
            binding.transactionListHome.visibility = View.GONE
            binding.emptyTransactionLayout.visibility = View.VISIBLE
        } else {
            binding.todaytransaction.visibility = View.GONE
            binding.transactionListHome.visibility = View.VISIBLE
            binding.emptyTransactionLayout.visibility = View.GONE

            binding.transactionListHome.layoutManager = LinearLayoutManager(requireContext())
            val adapter = TransactionAdapter(todayTransactions) { transaction ->
                val detailFragment = TransactionDetailFragment.newInstance(transaction)
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, detailFragment)
                    .addToBackStack(null)
                    .commit()
            }
            binding.transactionListHome.adapter = adapter
        }
        val dateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.forLanguageTag("vi-VN"))
        binding.today.text = dateFormat.format(Date())

        loadTaskSummary(binding)
    }

    private fun loadTaskSummary(binding: HomefragmentBinding) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val tasks = RetrofitClient.api.getTasks()
                val total = tasks.size
                val done = tasks.count { it.completed }

                binding.totaltaskhome.text = "$done/$total đã hoàn thành"
                binding.taskProgressHome.progress = if (total == 0) 0 else done * 100 / total
                binding.taskStatusHome.text =
                    if (total == 0) getString(R.string.home_no_tasks)
                    else "Còn ${total - done} công việc chưa xong"
            } catch (e: Exception) {
                android.util.Log.e("TaskAPI", "Lỗi tải công việc (Home)", e)
                binding.taskStatusHome.text = "Không tải được công việc: ${e.message}"
            }
        }
    }



}
