package com.sumit.payrollmanagement.payroll.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.payrollmanagement.payroll.domain.model.EmployeeInput
import com.sumit.payrollmanagement.payroll.domain.usecase.CreatePayrollUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePayrollViewModel @Inject constructor(
    private val createPayroll: CreatePayrollUseCase
) : ViewModel() {
    private val mutableState = MutableStateFlow(CreatePayrollUiState())
    val state: StateFlow<CreatePayrollUiState> = mutableState.asStateFlow()

    private val effectsChannel = Channel<CreatePayrollEffect>(Channel.BUFFERED)
    val effects = effectsChannel.receiveAsFlow()

    private var nextFormId = 2L

    fun onAction(action: CreatePayrollAction) {
        when (action) {
            CreatePayrollAction.Back -> sendEffect(CreatePayrollEffect.NavigateBack)
            CreatePayrollAction.AddEmployee -> addEmployee()
            CreatePayrollAction.Submit -> submit()
            is CreatePayrollAction.RemoveEmployee -> removeEmployee(action.formId)
            is CreatePayrollAction.NameChanged -> updateEmployee(action.formId) {
                it.copy(name = action.value)
            }
            is CreatePayrollAction.WagesChanged -> updateEmployee(action.formId) {
                it.copy(wages = action.value.filterWagesInput())
            }
            is CreatePayrollAction.ExemptChanged -> updateEmployee(action.formId) {
                it.copy(isExempt = action.value)
            }
        }
    }

    private fun addEmployee() {
        mutableState.update {
            it.copy(
                employees = it.employees + EmployeeFormState(formId = nextFormId++),
                errorMessage = null
            )
        }
    }

    private fun removeEmployee(formId: Long) {
        mutableState.update { state ->
            if (state.employees.size == 1) state else state.copy(
                employees = state.employees.filterNot { it.formId == formId },
                errorMessage = null
            )
        }
    }

    private fun updateEmployee(
        formId: Long,
        transform: (EmployeeFormState) -> EmployeeFormState
    ) {
        mutableState.update { state ->
            state.copy(
                employees = state.employees.map {
                    if (it.formId == formId) transform(it) else it
                },
                errorMessage = null
            )
        }
    }

    private fun submit() {
        if (mutableState.value.isSaving) return

        viewModelScope.launch {
            mutableState.update { it.copy(isSaving = true, errorMessage = null) }
            runCatching {
                createPayroll(
                    mutableState.value.employees.map {
                        EmployeeInput(
                            name = it.name,
                            wages = it.wages,
                            isExempt = it.isExempt
                        )
                    }
                )
            }.onSuccess { payrollId ->
                mutableState.update { it.copy(isSaving = false) }
                effectsChannel.send(CreatePayrollEffect.PayrollCreated(payrollId))
            }.onFailure { error ->
                mutableState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = error.message ?: "Unable to create payroll"
                    )
                }
            }
        }
    }

    private fun sendEffect(effect: CreatePayrollEffect) {
        viewModelScope.launch { effectsChannel.send(effect) }
    }

    private fun String.filterWagesInput(): String {
        val filtered = filter { it.isDigit() || it == '.' }
        val decimalIndex = filtered.indexOf('.')
        return if (decimalIndex == -1) {
            filtered
        } else {
            filtered.take(decimalIndex + 1) +
                filtered.drop(decimalIndex + 1).filter(Char::isDigit).take(2)
        }
    }
}
