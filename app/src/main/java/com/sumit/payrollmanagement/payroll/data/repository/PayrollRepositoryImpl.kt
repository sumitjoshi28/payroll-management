package com.sumit.payrollmanagement.payroll.data.repository

import com.sumit.payrollmanagement.payroll.data.local.PayrollDao
import com.sumit.payrollmanagement.payroll.data.mapper.toDomain
import com.sumit.payrollmanagement.payroll.data.mapper.toEntity
import com.sumit.payrollmanagement.payroll.data.remote.PayrollApi
import com.sumit.payrollmanagement.payroll.domain.model.Payroll
import com.sumit.payrollmanagement.payroll.domain.repository.PayrollRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PayrollRepositoryImpl @Inject constructor(
    private val dao: PayrollDao,
    private val api: PayrollApi
) : PayrollRepository {
    override fun observePayrolls(): Flow<List<Payroll>> =
        dao.observePayrolls().map { payrolls -> payrolls.map { it.toDomain() } }

    override fun observePayroll(id: Long): Flow<Payroll?> =
        dao.observePayroll(id).map { it?.toDomain() }

    override suspend fun createPayroll(payroll: Payroll): Long {
        val payrollId = dao.insertPayrollWithEmployees(
            payroll = payroll.toEntity(),
            employees = payroll.employees.mapIndexed { index, employee ->
                employee.toEntity(position = index)
            }
        )

        runCatching { api.uploadPayroll(payroll.copy(id = payrollId)) }
        return payrollId
    }
}
