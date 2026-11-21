package com.example.demov1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.demov1.R
import com.example.demov1.databinding.ItemTransactionBinding
import com.example.demov1.databinding.ItemTransactionDateHeaderBinding
import com.example.demov1.model.Transaction
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransactionAdapter(
    transactions: List<Transaction>,
    private val choiceitem: (Transaction) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private sealed class ListItem {
        data class DateHeader(val label: String, val count: Int) : ListItem()
        data class TransactionRow(val transaction: Transaction) : ListItem()
    }

    private val items = mutableListOf<ListItem>()

    init {
        updateTransactions(transactions)
    }

    fun updateTransactions(transactions: List<Transaction>) {
        items.clear()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("vi-VN"))

        val todayCal = Calendar.getInstance()
        val todayStr = formatter.format(todayCal.time)

        val yesterdayCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        val yesterdayStr = formatter.format(yesterdayCal.time)

        val grouped = transactions
            .groupBy { it.date.trim() }
            .entries
            .sortedByDescending { entry ->
                try {
                    formatter.parse(entry.key)?.time ?: Long.MIN_VALUE
                } catch (_: Exception) {
                    Long.MIN_VALUE
                }
            }

        for ((date, dayTransactions) in grouped) {
            val headerLabel = when (date) {
                todayStr -> "Hôm nay • $date"
                yesterdayStr -> "Hôm qua • $date"
                else -> date
            }
            items.add(ListItem.DateHeader(headerLabel, dayTransactions.size))
            dayTransactions.forEach { transaction ->
                items.add(ListItem.TransactionRow(transaction))
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ListItem.DateHeader -> TYPE_HEADER
            is ListItem.TransactionRow -> TYPE_TRANSACTION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            val headerBinding = ItemTransactionDateHeaderBinding.inflate(inflater, parent, false)
            HeaderViewHolder(headerBinding)
        } else {
            val rowBinding = ItemTransactionBinding.inflate(inflater, parent, false)
            TransactionViewHolder(rowBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ListItem.DateHeader -> {
                (holder as HeaderViewHolder).bind(item.label, item.count)
            }
            is ListItem.TransactionRow -> {
                val transaction = item.transaction
                (holder as TransactionViewHolder).bind(transaction)
                holder.itemView.setOnClickListener {
                    choiceitem(transaction)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class HeaderViewHolder(private val binding: ItemTransactionDateHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(label: String, count: Int) {
            binding.tvDateHeader.text = label
            binding.tvDateCount.text = "$count giao dịch"
        }
    }

    class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.transactionName.text = transaction.title.ifEmpty { transaction.id }
            binding.transactionDate.text = transaction.date
            binding.transactionAmount.text = transaction.amount.toString()
            binding.transactionType.text = transaction.type

            val isIncome =
                transaction.type == binding.root.context.getString(R.string.transaction_income) ||
                    transaction.amount >= 0

            val context = binding.root.context
            if (isIncome) {
                binding.root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.income_card_bg))
                binding.root.strokeColor = ContextCompat.getColor(context, R.color.income_card_stroke)
                binding.transactionAmount.setTextColor(ContextCompat.getColor(context, R.color.income_amount_text))
                binding.transactionIcon.setImageResource(R.drawable.income)
            } else {
                binding.root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.expense_card_bg))
                binding.root.strokeColor = ContextCompat.getColor(context, R.color.expense_card_stroke)
                binding.transactionAmount.setTextColor(ContextCompat.getColor(context, R.color.expense_amount_text))
                binding.transactionIcon.setImageResource(R.drawable.spending)
            }
        }
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_TRANSACTION = 1
    }
}
