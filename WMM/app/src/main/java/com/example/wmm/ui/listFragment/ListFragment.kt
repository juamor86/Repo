package com.example.wmm.ui.listFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.wmm.R
import com.example.wmm.databinding.FragmentListBinding
import com.example.wmm.model.entities.ExpenseEntity
import com.example.wmm.model.entities.TransactionType
import com.example.wmm.ui.base.BaseAdapter
import com.example.wmm.ui.base.GenericSimpleRecyclerBindingInterface
import com.example.wmm.utils.DateUtil

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null

    private val binding get() = _binding!!
    private val listViewModel: ListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        setupDataRecyclerView()
        loadSelectedTransactionType(binding.expenseDateTabRg.checkedRadioButtonId)
        setupRadioGroup()
        return binding.root
    }

    private fun setupDataRecyclerView() {
        listViewModel.transactionLiveData.observe(viewLifecycleOwner) {
            it?.let { expenseList ->
                showRecyclerView(expenseList)
            }
        }
    }

    private fun setupRadioGroup() {
        binding.expenseDateTabRg.setOnCheckedChangeListener { _, checkedId ->
            loadSelectedTransactionType(checkedId)
        }
    }

    private fun loadSelectedTransactionType(checkedId: Int) {
        with(listViewModel) {
            when (checkedId) {
                R.id.this_week_rb -> loadThisWeekTransactions()
                R.id.this_month_rb -> loadThisMonthTransaction()
                else -> loadAllTransaction()
            }
        }
    }

    private fun showRecyclerView(expenseList: List<ExpenseEntity>) {
        binding.expenseListRv.adapter = BaseAdapter(
            expenseList,
            R.layout.expense_item_view,
            getRecyclerViewBinding()
        )
    }

    private fun getRecyclerViewBinding(): GenericSimpleRecyclerBindingInterface<ExpenseEntity> {
        return object : GenericSimpleRecyclerBindingInterface<ExpenseEntity> {
            override fun bindData(item: ExpenseEntity, view: View) {
                with(view) {
                    findViewById<TextView>(R.id.expense_name_item_tv).text = item.name
                    findViewById<TextView>(R.id.expense_date_item_tv).text = DateUtil.parseDateToStringShortDate(item.date)
                    findViewById<TextView>(R.id.expense_quantity_item_tv).apply {
                        if (item.type == TransactionType.EXPENSE) {
                            text = "- ${item.amount} €"
                            setTextColor(context.getColor(R.color.sunglo))
                        } else {
                            text = "${item.amount} €"
                            setTextColor(context.getColor(R.color.de_york))
                        }
                    }
                }
            }
        }
    }
}