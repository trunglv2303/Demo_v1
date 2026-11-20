package com.example.demov1.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demov1.R
import com.example.demov1.databinding.ItemTransactionBinding
import com.example.demov1.model.Transaction

class TransactionAdapter(
    private val transactions: List<Transaction>,
    private val choiceitem : (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rowBinding= ItemTransactionBinding.inflate(inflater, parent, false)
        val holder = ViewHolder(rowBinding)
        return holder

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
        holder.itemView.setOnClickListener {
            choiceitem(transaction)
        }

    }

    override fun getItemCount(): Int = transactions.size



    class ViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.transactionName.text = transaction.id.toString()
            binding.transactionDate.text = transaction.date
            binding.transactionAmount.text = transaction.amount.toString()
            binding.transactionType.text = transaction.type
            val isIncome = transaction.type == binding.root.context.getString(R.string.transaction_income)                    || transaction.amount >= 0
            if (isIncome) {
                binding.root.setCardBackgroundColor(Color.parseColor("#ECFDF3"))
                binding.root.strokeColor = Color.parseColor("#BBF7D0")
                binding.transactionAmount.setTextColor(Color.parseColor("#15803D"))
            } else {
                binding.root.setCardBackgroundColor(Color.parseColor("#FEF2F2"))
                binding.root.strokeColor = Color.parseColor("#FECACA")
                binding.transactionAmount.setTextColor(Color.parseColor("#DC2626"))
            }


    }}}
