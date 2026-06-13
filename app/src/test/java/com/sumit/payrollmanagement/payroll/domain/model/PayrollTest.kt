package com.sumit.payrollmanagement.payroll.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class PayrollTest {
    @Test
    fun `summary total includes wages from every employee`() {
        val payroll = Payroll(
            createdAtEpochMillis = 0,
            employees = listOf(
                EmployeePay(
                    name = "First employee",
                    wagesCents = 10_000,
                    isExempt = false,
                    taxCents = 0
                ),
                EmployeePay(
                    name = "Second employee",
                    wagesCents = 20_000,
                    isExempt = false,
                    taxCents = 0
                )
            )
        )

        assertEquals(30_000L, payroll.totalWagesCents)
        assertEquals(30_000L, payroll.totalNetCents)
    }
}
