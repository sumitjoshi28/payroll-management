package com.sumit.payrollmanagement.payroll.presentation.list

import com.sumit.payrollmanagement.payroll.domain.model.Payroll

data class PayrollListUiState(
    val isLoading: Boolean = true,
    val payrolls: List<Payroll> = emptyList()
)

sealed interface PayrollListAction {
    data object CreatePayroll : PayrollListAction
    data class OpenPayroll(val id: Long) : PayrollListAction
}

sealed interface PayrollListEffect {
    data object NavigateToCreate : PayrollListEffect
    data class NavigateToDetail(val id: Long) : PayrollListEffect
}
