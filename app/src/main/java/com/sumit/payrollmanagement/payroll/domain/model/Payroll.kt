package com.sumit.payrollmanagement.payroll.domain.model

data class Payroll(
    val id: Long = 0,
    val createdAtEpochMillis: Long,
    val employees: List<EmployeePay>
) {
    val totalWagesCents: Long
        get() = employees.sumOf(EmployeePay::wagesCents)

    val totalTaxCents: Long
        get() = employees.sumOf(EmployeePay::taxCents)

    val totalNetCents: Long
        get() = employees.sumOf(EmployeePay::netCents)
}

data class EmployeePay(
    val id: Long = 0,
    val name: String,
    val wagesCents: Long,
    val isExempt: Boolean,
    val taxCents: Long
) {
    val netCents: Long = wagesCents - taxCents
}

data class EmployeeInput(
    val name: String,
    val wages: String,
    val isExempt: Boolean
)
