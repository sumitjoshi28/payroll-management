package com.sumit.payrollmanagement.payroll.presentation.create

data class EmployeeFormState(
    val formId: Long,
    val name: String = "",
    val wages: String = "",
    val isExempt: Boolean = false
)

data class CreatePayrollUiState(
    val employees: List<EmployeeFormState> = listOf(EmployeeFormState(formId = 1)),
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)

sealed interface CreatePayrollAction {
    data object Back : CreatePayrollAction
    data object AddEmployee : CreatePayrollAction
    data object Submit : CreatePayrollAction
    data class RemoveEmployee(val formId: Long) : CreatePayrollAction
    data class NameChanged(val formId: Long, val value: String) : CreatePayrollAction
    data class WagesChanged(val formId: Long, val value: String) : CreatePayrollAction
    data class ExemptChanged(val formId: Long, val value: Boolean) : CreatePayrollAction
}

sealed interface CreatePayrollEffect {
    data object NavigateBack : CreatePayrollEffect
    data class PayrollCreated(val payrollId: Long) : CreatePayrollEffect
}
