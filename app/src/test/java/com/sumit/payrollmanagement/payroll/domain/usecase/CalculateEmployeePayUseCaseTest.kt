package com.sumit.payrollmanagement.payroll.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateEmployeePayUseCaseTest {
    private val useCase = CalculateEmployeePayUseCase()

    @Test
    fun `wages above threshold are taxed at five percent`() {
        val employee = useCase(
            name = "James Caldwell",
            wagesCents = 190_000,
            isExempt = false
        )

        assertEquals(9_500, employee.taxCents)
        assertEquals(180_500, employee.netCents)
    }

    @Test
    fun `wages exactly at threshold are not taxed`() {
        val employee = useCase(
            name = "Sarah Mitchell",
            wagesCents = 100_000,
            isExempt = false
        )

        assertEquals(0, employee.taxCents)
        assertEquals(100_000, employee.netCents)
    }

    @Test
    fun `exempt employee is not taxed above threshold`() {
        val employee = useCase(
            name = "Laura Nguyen",
            wagesCents = 200_000,
            isExempt = true
        )

        assertEquals(0, employee.taxCents)
        assertEquals(200_000, employee.netCents)
    }
}
