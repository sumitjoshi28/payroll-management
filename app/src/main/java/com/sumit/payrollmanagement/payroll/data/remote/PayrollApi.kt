package com.sumit.payrollmanagement.payroll.data.remote

import com.sumit.payrollmanagement.payroll.domain.model.Payroll

interface PayrollApi {
    suspend fun uploadPayroll(payroll: Payroll): Result<Unit>
}
