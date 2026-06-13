package com.sumit.payrollmanagement.payroll.presentation.detail

import com.sumit.payrollmanagement.payroll.domain.model.Payroll

data class PayrollDetailUiState(
    val isLoading: Boolean = true,
    val payroll: Payroll? = null
)

sealed interface PayrollDetailAction {
    data object Back : PayrollDetailAction
}

sealed interface PayrollDetailEffect {
    data object NavigateBack : PayrollDetailEffect
}
