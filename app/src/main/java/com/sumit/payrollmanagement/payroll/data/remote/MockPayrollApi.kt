package com.sumit.payrollmanagement.payroll.data.remote

import com.sumit.payrollmanagement.payroll.domain.model.Payroll
import javax.inject.Inject

class MockPayrollApi @Inject constructor() : PayrollApi {
    override suspend fun uploadPayroll(payroll: Payroll): Result<Unit> = Result.success(Unit)
}
