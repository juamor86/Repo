package com.example.wmm.ui.homeFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wmm.model.entities.DayType
import com.example.wmm.model.entities.ExpenseEntity
import com.example.wmm.model.entities.MonthType
import com.example.wmm.model.entities.TransactionResume
import com.example.wmm.model.entities.TransactionType
import com.example.wmm.model.entities.WeekType
import com.example.wmm.model.usecases.GetExpensesByTimeIntervalUseCase
import com.example.wmm.utils.Consts
import com.example.wmm.utils.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesByTimeIntervalUseCase
) : ViewModel() {
    val resumeLiveData = MutableLiveData<List<TransactionResume>>()
    val lastTransactionsLiveData = MutableLiveData<List<ExpenseEntity>>()

    fun getYearData() {
        viewModelScope.launch {
            val allTransactions = getExpensesUseCase.invoke()
            resumeLiveData.postValue(groupDataByMonth(allTransactions))
        }
    }

    fun getThisMonthData() {
        viewModelScope.launch {
            val allTransactions = getExpensesUseCase.invoke()
            resumeLiveData.postValue(groupDataByWeekOfMonth(allTransactions))
        }
    }

    fun getThisWeekData() {
        viewModelScope.launch {
            val allTransactions = getExpensesUseCase.invoke()
            resumeLiveData.postValue(groupDataByDayOfWeek(allTransactions))
        }
    }

    fun getLastTransactions() {
        viewModelScope.launch {
            var allTransactions = getExpensesUseCase.invoke().sortedByDescending { it.date.time }
            allTransactions = allTransactions.take(Consts.HOME_NUMBER_TRANSACTIONS)
            lastTransactionsLiveData.postValue(allTransactions)
        }
    }

    //TODO improve this code
    private fun groupDataByMonth(allTransactions: List<ExpenseEntity>): List<TransactionResume> {
        val resumeAnualList = mutableListOf<TransactionResume>()
        val thisYearTransactionList =
            allTransactions.filter { DateUtil.isDateInThisYear(it.date) }.sortedBy { it.date.time }
        thisYearTransactionList.forEach { transaction ->
            with(Calendar.getInstance()) {
                time = transaction.date
                get(Calendar.MONTH).let { actualMonth ->
                    val resume = resumeAnualList.find { it.month?.id == actualMonth }
                    if (transaction.type == TransactionType.EXPENSE) {
                        if (resume != null) {
                            resume.expenseAmount += transaction.amount
                        } else {
                            resumeAnualList.add(
                                TransactionResume(
                                    month = MonthType.getMonthType(actualMonth)!!,
                                    expenseAmount = transaction.amount
                                )
                            )
                        }
                    } else {
                        if (resume != null) {
                            resume.incomeAmount += transaction.amount
                        } else {
                            resumeAnualList.add(
                                TransactionResume(
                                    month = MonthType.getMonthType(actualMonth)!!,
                                    incomeAmount = transaction.amount
                                )
                            )
                        }
                    }
                }
            }
        }
        return resumeAnualList.toList()
    }

    private fun groupDataByWeekOfMonth(allTransactions: List<ExpenseEntity>): List<TransactionResume> {
        val resumeAnualList = mutableListOf<TransactionResume>()
        val weekOfMonthList =
            allTransactions.filter { DateUtil.isDateInThisMonth(it.date) }.sortedBy { it.date.time }
        weekOfMonthList.forEach { transaction ->
            with(Calendar.getInstance()) {
                time = transaction.date
                get(Calendar.WEEK_OF_MONTH).let { weekOfMonth ->
                    val resume = resumeAnualList.find { it.weekOfMonth?.id == weekOfMonth }
                    if (transaction.type == TransactionType.EXPENSE) {
                        if (resume != null) {
                            resume.expenseAmount += transaction.amount
                        } else {
                            resumeAnualList.add(
                                TransactionResume(
                                    weekOfMonth = WeekType.getWeekOfMonth(weekOfMonth)!!,
                                    expenseAmount = transaction.amount
                                )
                            )
                        }
                    } else {
                        if (resume != null) {
                            resume.incomeAmount += transaction.amount
                        } else {
                            resumeAnualList.add(
                                TransactionResume(
                                    weekOfMonth = WeekType.getWeekOfMonth(weekOfMonth)!!,
                                    incomeAmount = transaction.amount
                                )
                            )
                        }
                    }

                }
            }
        }
        return resumeAnualList.toList()
    }

    private fun groupDataByDayOfWeek(allTransactions: List<ExpenseEntity>): List<TransactionResume> {
        val resumeAnualList = mutableListOf<TransactionResume>()
        val weekOfMonthList =
            allTransactions.filter { DateUtil.isDateInThisWeek(it.date) }.sortedBy { it.date.time }
        weekOfMonthList.forEach { transaction ->
            with(Calendar.getInstance()) {
                firstDayOfWeek = Calendar.MONDAY
                time = transaction.date
                get(Calendar.DAY_OF_WEEK).let { dayOfWeek ->
                    val resume = resumeAnualList.find { it.dayOfWeek?.id == dayOfWeek }
                    if (transaction.type == TransactionType.EXPENSE) {
                        if (resume != null) {
                            resume.expenseAmount += transaction.amount
                        } else {
                            resumeAnualList.add(
                                TransactionResume(
                                    dayOfWeek = DayType.getDayType(dayOfWeek)!!,
                                    expenseAmount = transaction.amount
                                )
                            )
                        }
                    } else {
                        if (resume != null) {
                            resume.incomeAmount += transaction.amount
                        } else {
                            resumeAnualList.add(
                                TransactionResume(
                                    dayOfWeek = DayType.getDayType(dayOfWeek)!!,
                                    incomeAmount = transaction.amount
                                )
                            )
                        }
                    }
                }
            }
        }
        return resumeAnualList.toList()
    }
}