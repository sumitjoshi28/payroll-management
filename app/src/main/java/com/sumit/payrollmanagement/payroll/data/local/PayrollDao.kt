package com.sumit.payrollmanagement.payroll.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PayrollDao {
    @Transaction
    @Query("SELECT * FROM payrolls ORDER BY createdAtEpochMillis DESC")
    abstract fun observePayrolls(): Flow<List<PayrollWithEmployees>>

    @Transaction
    @Query("SELECT * FROM payrolls WHERE id = :id")
    abstract fun observePayroll(id: Long): Flow<PayrollWithEmployees?>

    @Insert
    protected abstract suspend fun insertPayroll(payroll: PayrollEntity): Long

    @Insert
    protected abstract suspend fun insertEmployees(employees: List<EmployeeEntity>)

    @Transaction
    open suspend fun insertPayrollWithEmployees(
        payroll: PayrollEntity,
        employees: List<EmployeeEntity>
    ): Long {
        val payrollId = insertPayroll(payroll)
        insertEmployees(employees.map { it.copy(payrollId = payrollId) })
        return payrollId
    }
}
