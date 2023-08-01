package com.example.wmm.ui.addFragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.wmm.R
import com.example.wmm.databinding.FragmentAddBinding
import com.example.wmm.model.entities.ExpenseEntity
import com.example.wmm.model.entities.TransactionType
import com.example.wmm.utils.DateUtil
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar

class AddFragment : Fragment() {

    private lateinit var categoryAdapter: ArrayAdapter<String>
    private var _binding: FragmentAddBinding? = null

    private val binding get() = _binding!!
    private val addViewModel: AddViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadExpenseAdapter()
    }

    //TODO change this
    private fun loadExpenseAdapter() {
        val items = listOf(
            "Ocio",
            "Seguros",
            "Préstamo",
            "Alquiler",
            "Servicio streaming",
            "Combustible",
            "Luz",
            "Compras online",
            "Otro"
        )
        categoryAdapter = ArrayAdapter(requireContext(), R.layout.expense_item_dropdown, items)
    }

    private fun loadIncomeAdapter() {
        val items = listOf("Nómina", "Cheque", "Bizum", "Ingreso", "Efectivo", "Otro")
        categoryAdapter = ArrayAdapter(requireContext(), R.layout.expense_item_dropdown, items)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        setupDateField()
        setupAddButton()
        setupDropDownMenu()
        setupRadioButtonGroup()
    }

    private fun setupRadioButtonGroup() {
        binding.expenseTypeRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.expense_rb -> loadExpenseAdapter()
                else -> loadIncomeAdapter()
            }
            setupDropDownMenu()
        }
    }

    private fun setupAddButton() {
        binding.addExpenseBt.setOnClickListener {
            saveExpense()
        }
    }

    private fun clearData() {
        with(binding){
            expenseNameEt.text?.clear()
            expenseQuantityEt.text?.clear()
            setupDateField()
            expenseDescriptionEt.text?.clear()
            setupDropDownMenu()
            expenseTypeRg.check(R.id.expense_rb)
        }
    }

    private fun showSnackBar() {
        val snackbar: Snackbar =
            Snackbar.make(requireView(), R.string.transaction_added_success, 2000)
        snackbar.view.layoutParams =
            (snackbar.view.layoutParams as FrameLayout.LayoutParams).apply {
                gravity = Gravity.TOP
            }
        snackbar.show()
    }

    private fun saveExpense() {
        if (formatAllData()) {
            with(binding) {
                addViewModel.saveExpense(
                    ExpenseEntity(
                        id = 1L,
                        name = expenseNameEt.text.toString(),
                        amount = expenseQuantityEt.text.toString().toFloat(),
                        date = DateUtil.parseStringShortDateToDate(expenseDateEt.text.toString()),
                        description = expenseDescriptionEt.text.toString(),
                        type = getTransactionType(),
                        category = expenseCategoryDropdownTf.editText!!.text.toString()
                    )
                )
            }
            clearData()
            showSnackBar()
        }
    }

    private fun getTransactionType(): TransactionType {
        return when (binding.expenseTypeRg.checkedRadioButtonId) {
            R.id.income_rb -> TransactionType.INCOME
            else -> TransactionType.EXPENSE
        }
    }

    private fun formatAllData(): Boolean {
        val isNameCorrect = formatName()
        val isAmountCorrect = formatAmount()
        val isTypeCorrect = formatType()
        return isNameCorrect && isAmountCorrect && isTypeCorrect
    }

    private fun formatType(): Boolean {
        val typeFieldText = binding.expenseCategoryDropdownTf.editText!!.text ?: ""
        typeFieldText.trim()
        return if (typeFieldText.isEmpty()) {
            binding.expenseCategoryDropdownTf.error = getText(R.string.obligatory_error_text)
            false
        } else {
            binding.expenseCategoryDropdownTf.error = null
            true
        }
    }

    private fun formatName(): Boolean {
        val nameFieldText = binding.expenseNameEt.text ?: ""
        nameFieldText.trim()
        return if (nameFieldText.isEmpty()) {
            binding.expenseNameTf.error = getText(R.string.obligatory_error_text)
            false
        } else {
            binding.expenseNameTf.error = null
            true
        }
    }

    private fun formatAmount(): Boolean {
        val amountFieldText = binding.expenseQuantityEt.text ?: ""
        amountFieldText.trim()
        return if (amountFieldText.isEmpty()) {
            binding.expenseQuantityTf.error = getText(R.string.obligatory_error_text)
            false
        } else {
            if (amountFieldText.toString().toFloat().compareTo(0) == 0) {
                binding.expenseQuantityTf.error = getText(R.string.not_0_error_text)
                false
            } else {
                binding.expenseQuantityTf.error = null
                true
            }
        }
    }

    private fun setupDropDownMenu() {
        (binding.expenseCategoryDropdownTf.editText as? AutoCompleteTextView)?.setAdapter(
            categoryAdapter
        )
    }

    private fun setupDateField() {
        binding.expenseDateEt.apply {
            setText(DateUtil.getTodayShortDate())
            setOnClickListener {
                setupDatePicker()
            }
        }
    }

    private fun setupDatePicker() {
        val datePicker =
            MaterialDatePicker
                .Builder
                .datePicker()
                .setTitleText(getText(R.string.select_date_text))
                .build()

        datePicker.show(parentFragmentManager, "tag")
        datePicker.addOnPositiveButtonClickListener { dateMillis ->
            binding.expenseDateEt.setText(DateUtil.parseLongToStringShortDate(dateMillis))
        }
    }

}