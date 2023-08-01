package com.example.wmm.ui.listFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wmm.model.entities.ExpenseEntity
import com.example.wmm.model.usecases.GetExpensesUseCase
import com.example.wmm.utils.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase
) : ViewModel() {

    val transactionLiveData = MutableLiveData<List<ExpenseEntity>>()

    fun loadAllTransaction() {
        viewModelScope.launch {
            val transactionList = getExpensesUseCase.invoke().sortedByDescending { it.date.time }
            transactionLiveData.postValue(transactionList)
        }
    }

    fun loadThisWeekTransactions() {
        viewModelScope.launch {
            val transactionList = getExpensesUseCase.invoke().filter {
                DateUtil.isDateInThisWeek(it.date)
            }.sortedBy { it.date.time }
            transactionLiveData.postValue(transactionList)
        }
    }

    fun loadThisMonthTransaction() {
        viewModelScope.launch {
            val transactionList = getExpensesUseCase.invoke().filter {
                DateUtil.isDateInThisMonth(it.date)
            }.sortedBy { it.date.time }
            transactionLiveData.postValue(transactionList)
        }
    }
}