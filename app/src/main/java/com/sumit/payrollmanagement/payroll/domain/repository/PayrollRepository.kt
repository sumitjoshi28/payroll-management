package com.sumit.payrollmanagement.payroll.domain.repository

import com.sumit.payrollmanagement.payroll.domain.model.Payroll
import kotlinx.coroutines.flow.Flow

interface PayrollRepository {
    fun observePayrolls(): Flow<List<Payroll>>
    fun observePayroll(id: Long): Flow<Payroll?>
    suspend fun createPayroll(payroll: Payroll): Long
}
