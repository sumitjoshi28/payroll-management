package com.sumit.payrollmanagement.payroll.domain.usecase

import com.sumit.payrollmanagement.payroll.domain.model.Payroll
import com.sumit.payrollmanagement.payroll.domain.repository.PayrollRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePayrollsUseCase @Inject constructor(
    private val repository: PayrollRepository
) {
    operator fun invoke(): Flow<List<Payroll>> = repository.observePayrolls()
}

class ObservePayrollUseCase @Inject constructor(
    private val repository: PayrollRepository
) {
    operator fun invoke(id: Long): Flow<Payroll?> = repository.observePayroll(id)
}
