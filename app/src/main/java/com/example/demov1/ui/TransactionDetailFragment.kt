package com.example.demov1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.demov1.databinding.FragmentTransactionDetailBinding
import com.example.demov1.model.Transaction

class TransactionDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)

        val name = arguments?.getString(EXTRA_ID).orEmpty()
        val amount = arguments?.getDouble(EXTRA_AMOUNT) ?: 0.0
        val type = arguments?.getString(EXTRA_TYPE).orEmpty()
        val date = arguments?.getString(EXTRA_DATE).orEmpty()
        val imageUrl = arguments?.getString(EXTRA_IMAGE_URL).orEmpty()

         binding.transactionName.text = name
         binding.transactionAmount.text = amount.toString()
         binding.transactionType.text = type
         binding.transactionDate.text = date


        return binding.root
    }

    companion object {
        private const val EXTRA_ID = "transaction.id"
        private const val EXTRA_AMOUNT = "transaction.amount"
        private const val EXTRA_TYPE = "transaction.type"
        private const val EXTRA_DATE = "transaction.date"
        private const val EXTRA_IMAGE_URL = "transaction.image_url"

        fun newInstance(transaction: Transaction): TransactionDetailFragment {
            val fragment = TransactionDetailFragment()
            val bundle = Bundle().apply {
                putString(EXTRA_ID, transaction.id)
                putDouble(EXTRA_AMOUNT, transaction.amount)
                putString(EXTRA_TYPE, transaction.type)
                putString(EXTRA_DATE, transaction.date)
                putString(EXTRA_IMAGE_URL, transaction.imageUrl)
            }
            fragment.arguments = bundle
            return fragment
        }

        fun putInt(key: String, value: String) {}
    }
}