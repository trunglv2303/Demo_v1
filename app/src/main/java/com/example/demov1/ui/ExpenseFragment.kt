package com.example.demov1.ui

import android.app.Dialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demov1.R
import com.example.demov1.adapter.TransactionAdapter
import com.example.demov1.databinding.DialogTransactionBinding
import com.example.demov1.databinding.FragmentExpenseBinding
import com.example.demov1.model.Transaction
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExpenseFragment : Fragment(R.layout.fragment_expense) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentExpenseBinding.bind(view)
        val sampleTransactions = mutableListOf(
            Transaction(
                id = R.string.transaction_sample_food,
                amount = -85_000.0,
                type = getString(R.string.transaction_expense),
                date = "19/11/2026",
                imageUrl = ""
            ),
            Transaction(
                id = R.string.transaction_sample_salary,
                amount = 8_500_000.0,
                type = getString(R.string.transaction_income),
                date = "18/11/2026",
                imageUrl = ""
            ),
            Transaction(
                id = R.string.transaction_sample_coffee,
                amount = -42_000.0,
                type = getString(R.string.transaction_expense),
                date = "17/11/2026",
                imageUrl = ""
            )
        )

        binding.transactionList.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionList.adapter = TransactionAdapter(sampleTransactions){
            transaction ->
            val detailFragment = TransactionDetailFragment.newInstance(transaction)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null) // Giúp bấm nút Back quay lại được danh sách giao dịch
                .commit()
        }



        binding.createTransaction.setOnClickListener {
            if (childFragmentManager.findFragmentByTag(CreateTransactionDialog.TAG) == null) {
                CreateTransactionDialog().showNow(childFragmentManager, CreateTransactionDialog.TAG)
            }
        }
    }
}

class CreateTransactionDialog : DialogFragment() {
    private val selectedDate = Calendar.getInstance()
    private var hasSelectedDate = false
    private var datePicker: DatePickerDialog? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogTransactionBinding.inflate(layoutInflater)
        if (savedInstanceState?.containsKey(DATE_KEY) == true) {
            selectedDate.timeInMillis = savedInstanceState.getLong(DATE_KEY)
            hasSelectedDate = true
        }

        fun showSelectedDate() {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("vi-VN"))
            binding.selectDate.text = getString(
                R.string.transaction_selected_date, formatter.format(selectedDate.time)
            )
        }

        if (hasSelectedDate) showSelectedDate()
        binding.selectDate.setOnClickListener {
            datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    selectedDate.set(year, month, day)
                    hasSelectedDate = true
                    showSelectedDate()
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
            ).also { it.show() }
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.transaction_create)
            .setView(binding.root)
            .setNegativeButton(R.string.transaction_close, null)
            .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (hasSelectedDate) outState.putLong(DATE_KEY, selectedDate.timeInMillis)
    }

    override fun onDestroyView() {
        datePicker?.dismiss()
        datePicker = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "create_transaction"
        private const val DATE_KEY = "selected_date"
    }
}
