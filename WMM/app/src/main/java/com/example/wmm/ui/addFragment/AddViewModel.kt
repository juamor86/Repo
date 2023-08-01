package com.example.wmm.ui.addFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wmm.model.entities.ExpenseEntity
import com.example.wmm.model.usecases.SaveExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val saveExpenseUseCase: SaveExpenseUseCase
) : ViewModel() {

    fun saveExpense(expenseEntity: ExpenseEntity) {

        viewModelScope.launch(Dispatchers.IO){
            saveExpenseUseCase.params(expenseEntity).invoke()
        }
    }

}