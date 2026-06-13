package com.sumit.payrollmanagement.payroll.data.mapper

import com.sumit.payrollmanagement.payroll.data.local.EmployeeEntity
import com.sumit.payrollmanagement.payroll.data.local.PayrollEntity
import com.sumit.payrollmanagement.payroll.data.local.PayrollWithEmployees
import com.sumit.payrollmanagement.payroll.domain.model.EmployeePay
import com.sumit.payrollmanagement.payroll.domain.model.Payroll

fun PayrollWithEmployees.toDomain(): Payroll = Payroll(
    id = payroll.id,
    createdAtEpochMillis = payroll.createdAtEpochMillis,
    employees = employees.sortedBy(EmployeeEntity::position).map(EmployeeEntity::toDomain)
)

fun EmployeeEntity.toDomain(): EmployeePay = EmployeePay(
    id = id,
    name = name,
    wagesCents = wagesCents,
    isExempt = isExempt,
    taxCents = taxCents
)

fun Payroll.toEntity(): PayrollEntity = PayrollEntity(
    id = id,
    createdAtEpochMillis = createdAtEpochMillis
)

fun EmployeePay.toEntity(
    payrollId: Long = 0,
    position: Int = 0
): EmployeeEntity = EmployeeEntity(
    id = id,
    payrollId = payrollId,
    position = position,
    name = name,
    wagesCents = wagesCents,
    isExempt = isExempt,
    taxCents = taxCents
)
