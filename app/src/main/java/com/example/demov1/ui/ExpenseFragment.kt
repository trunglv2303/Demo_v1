package com.example.demov1.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demov1.R
import com.example.demov1.adapter.TransactionAdapter
import com.example.demov1.data.DataManager
import com.example.demov1.databinding.DialogTransactionBinding
import com.example.demov1.databinding.FragmentExpenseBinding
import com.example.demov1.data.model.Transaction
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.NumberFormat
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExpenseFragment : Fragment(R.layout.fragment_expense) {
    private val selectedMonth = Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, 1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedMonth.timeInMillis = savedInstanceState?.getLong(MONTH_KEY, selectedMonth.timeInMillis)
            ?: selectedMonth.timeInMillis
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentExpenseBinding.bind(view)
        val locale = Locale.forLanguageTag("vi-VN")
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", locale).apply { isLenient = false }
        val currencyFormat = NumberFormat.getCurrencyInstance(locale).apply { maximumFractionDigits = 2 }
        binding.transactionList.layoutManager = LinearLayoutManager(requireContext())
        val adapter =
            TransactionAdapter(emptyList()) { transaction ->
                val detailFragment = TransactionDetailFragment.newInstance(transaction)
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, detailFragment)
                    .addToBackStack(null)
                    .commit()
            }
        binding.transactionList.adapter = adapter

        fun showMonth() {
            val year = selectedMonth.get(Calendar.YEAR)
            val month = selectedMonth.get(Calendar.MONTH)
            val boundary = Calendar.getInstance().apply {
                clear()
                set(year, month, 1)
            }
            val start = boundary.timeInMillis
            boundary.add(Calendar.MONTH, 1)
            val end = boundary.timeInMillis
            val transactions = DataManager.sampleTransactions.filter { transaction ->
                val date = dateFormat.parse(transaction.date.trim(), ParsePosition(0))
                date != null && date.time in start until end
            }
            val income = transactions.sumOf { it.amount.coerceAtLeast(0.0) }
            val expenses = -transactions.sumOf { it.amount.coerceAtMost(0.0) }

            binding.tvMonth.text = getString(
                R.string.transaction_month,
                month + 1,
                year
            )
            binding.tvBalance.text = currencyFormat.format(income - expenses)
            binding.tvIncome.text = currencyFormat.format(income)
            binding.tvExpenses.text = currencyFormat.format(expenses.coerceAtLeast(0.0))
            adapter.updateTransactions(transactions)
            binding.transactionList.isVisible = transactions.isNotEmpty()
            binding.emptyTransactions.isVisible = transactions.isEmpty()
        }

        childFragmentManager.setFragmentResultListener(
            CreateTransactionDialog.RESULT_KEY,
            viewLifecycleOwner
        ) { _, result ->
            selectedMonth.timeInMillis = result.getLong(CreateTransactionDialog.CREATED_DATE_KEY)
            selectedMonth.set(Calendar.DAY_OF_MONTH, 1)
            showMonth()
            binding.transactionList.scrollToPosition(0)
        }

        binding.btnPreviousMonth.setOnClickListener {
            selectedMonth.add(Calendar.MONTH, -1)
            showMonth()
            binding.transactionList.scrollToPosition(0)
        }
        binding.btnNextMonth.setOnClickListener {
            selectedMonth.add(Calendar.MONTH, 1)
            showMonth()
            binding.transactionList.scrollToPosition(0)
        }
        showMonth()
        binding.createTransaction.setOnClickListener {
            if (childFragmentManager.findFragmentByTag(CreateTransactionDialog.TAG) == null) {
                CreateTransactionDialog().showNow(childFragmentManager, CreateTransactionDialog.TAG)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong(MONTH_KEY, selectedMonth.timeInMillis)
        super.onSaveInstanceState(outState)
    }

    private companion object {
        const val MONTH_KEY = "transaction_month"
    }
}

class CreateTransactionDialog : DialogFragment() {
    private val selectedDate = Calendar.getInstance()
    private var datePicker: DatePickerDialog? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.ThemeOverlay_DemoV1_TransactionDialog
        )
        val binding = DialogTransactionBinding.inflate(LayoutInflater.from(builder.context))
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("vi-VN"))
        if (savedInstanceState?.containsKey(DATE_KEY) == true) {
            selectedDate.timeInMillis = savedInstanceState.getLong(DATE_KEY)
        }
        binding.closeTransaction.setOnClickListener { dismiss() }
        binding.createTransaction.setOnClickListener {
            val idText = binding.transactionId.text?.toString()?.trim().orEmpty()
            val amountText = binding.transactionAmount.text?.toString()?.trim().orEmpty()
            val isIncome = binding.transactionKind.checkedButtonId == R.id.income
            val rawAmount = amountText.toDoubleOrNull()
            if (rawAmount == null || !rawAmount.isFinite() || rawAmount <= 0) {
                binding.transactionAmountField.error = getString(R.string.transaction_invalid_amount)
                binding.transactionAmount.requestFocus()
                return@setOnClickListener
            }
            binding.transactionAmountField.error = null
            val finalAmount = if (isIncome) rawAmount else -rawAmount
            val type =
                if (isIncome) {
                    getString(R.string.transaction_income)
                } else {
                    getString(R.string.transaction_expense)
                }
            val dateText = formatter.format(selectedDate.time)
            val newTransaction =
                Transaction(
                    id = idText.toString(),
                    title = binding.transactionName.text?.toString()?.trim().orEmpty(),
                    amount = finalAmount,
                    type = type,
                    date = dateText,
                    imageUrl = "",
                )
            DataManager.addTransaction(newTransaction)
            parentFragmentManager.setFragmentResult(RESULT_KEY, Bundle().apply {
                putLong(CREATED_DATE_KEY, selectedDate.timeInMillis)
            })
            dismiss()
        }
        fun showSelectedDate() {
            binding.selectDate.text =
                getString(R.string.transaction_selected_date, formatter.format(selectedDate.time))
        }

        showSelectedDate()
        binding.selectDate.setOnClickListener {
            datePicker =
                DatePickerDialog(
                        requireContext(),
                        { _, year, month, day ->
                            selectedDate.set(year, month, day)
                            showSelectedDate()
                        },
                        selectedDate.get(Calendar.YEAR),
                        selectedDate.get(Calendar.MONTH),
                        selectedDate.get(Calendar.DAY_OF_MONTH),
                    )
                    .also { it.show() }
        }

        return builder
            .setView(binding.root)
            .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(DATE_KEY, selectedDate.timeInMillis)
    }

    override fun onDestroyView() {
        datePicker?.dismiss()
        datePicker = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "create_transaction"
        const val RESULT_KEY = "transaction_created"
        const val CREATED_DATE_KEY = "transaction_created_date"
        private const val DATE_KEY = "selected_date"
    }
}
