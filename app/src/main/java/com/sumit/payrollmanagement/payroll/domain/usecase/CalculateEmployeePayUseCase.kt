package com.sumit.payrollmanagement.payroll.domain.usecase

import com.sumit.payrollmanagement.payroll.domain.model.EmployeePay
import javax.inject.Inject

class CalculateEmployeePayUseCase @Inject constructor() {
    operator fun invoke(
        name: String,
        wagesCents: Long,
        isExempt: Boolean
    ): EmployeePay {
        val taxCents = if (wagesCents > TAX_THRESHOLD_CENTS && !isExempt) {
            (wagesCents * TAX_PERCENT + PERCENT_ROUNDING_OFFSET) / 100
        } else {
            0
        }

        return EmployeePay(
            name = name,
            wagesCents = wagesCents,
            isExempt = isExempt,
            taxCents = taxCents
        )
    }

    private companion object {
        const val TAX_THRESHOLD_CENTS = 100_000L
        const val TAX_PERCENT = 5L
        const val PERCENT_ROUNDING_OFFSET = 50L
    }
}
