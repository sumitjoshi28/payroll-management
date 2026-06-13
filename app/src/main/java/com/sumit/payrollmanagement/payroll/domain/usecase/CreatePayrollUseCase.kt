package com.sumit.payrollmanagement.payroll.domain.usecase

import com.sumit.payrollmanagement.payroll.domain.model.EmployeeInput
import com.sumit.payrollmanagement.payroll.domain.model.Payroll
import com.sumit.payrollmanagement.payroll.domain.repository.PayrollRepository
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Clock
import javax.inject.Inject

class CreatePayrollUseCase @Inject constructor(
    private val repository: PayrollRepository,
    private val calculateEmployeePay: CalculateEmployeePayUseCase,
    private val clock: Clock
) {
    suspend operator fun invoke(inputs: List<EmployeeInput>): Long {
        if (inputs.isEmpty()) {
            throw InvalidPayrollException("Add at least one employee")
        }

        val employees = inputs.mapIndexed { index, input ->
            val employeeNumber = index + 1
            val name = input.name.trim()
            if (name.isEmpty()) {
                throw InvalidPayrollException("Enter a name for employee $employeeNumber")
            }

            val wagesCents = input.wages.toWagesCentsOrNull()
                ?: throw InvalidPayrollException("Enter valid wages for $name")
            if (wagesCents <= 0) {
                throw InvalidPayrollException("Wages for $name must be greater than zero")
            }

            calculateEmployeePay(
                name = name,
                wagesCents = wagesCents,
                isExempt = input.isExempt
            )
        }

        return repository.createPayroll(
            Payroll(
                createdAtEpochMillis = clock.millis(),
                employees = employees
            )
        )
    }

    private fun String.toWagesCentsOrNull(): Long? = runCatching {
        trim()
            .takeIf(String::isNotEmpty)
            ?.let(::BigDecimal)
            ?.setScale(2, RoundingMode.HALF_UP)
            ?.movePointRight(2)
            ?.longValueExact()
    }.getOrNull()
}

class InvalidPayrollException(message: String) : IllegalArgumentException(message)
