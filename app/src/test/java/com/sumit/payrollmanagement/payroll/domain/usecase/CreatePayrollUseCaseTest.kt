package com.sumit.payrollmanagement.payroll.domain.usecase

import com.sumit.payrollmanagement.payroll.domain.model.EmployeeInput
import com.sumit.payrollmanagement.payroll.domain.model.Payroll
import com.sumit.payrollmanagement.payroll.domain.repository.PayrollRepository
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CreatePayrollUseCaseTest {
    private val repository = FakePayrollRepository()
    private val clock = Clock.fixed(Instant.parse("2026-06-12T10:00:00Z"), ZoneOffset.UTC)
    private val useCase = CreatePayrollUseCase(
        repository = repository,
        calculateEmployeePay = CalculateEmployeePayUseCase(),
        clock = clock
    )

    @Test
    fun `creates payroll with expected example totals`() = runBlocking {
        val id = useCase(
            listOf(
                EmployeeInput("Sarah Mitchell", "900", isExempt = true),
                EmployeeInput("James Caldwell", "1900", isExempt = false),
                EmployeeInput("Laura Nguyen", "2000", isExempt = false)
            )
        )

        val payroll = repository.createdPayroll
        assertEquals(42L, id)
        assertEquals(clock.millis(), payroll?.createdAtEpochMillis)
        assertEquals(480_000L, payroll?.totalWagesCents)
        assertEquals(19_500L, payroll?.totalTaxCents)
        assertEquals(460_500L, payroll?.totalNetCents)
    }

    @Test
    fun `rejects an employee without a name`() {
        val error = assertThrows(InvalidPayrollException::class.java) {
            runBlocking {
                useCase(listOf(EmployeeInput("", "1000", isExempt = false)))
            }
        }

        assertEquals("Enter a name for employee 1", error.message)
    }

    private class FakePayrollRepository : PayrollRepository {
        var createdPayroll: Payroll? = null

        override fun observePayrolls(): Flow<List<Payroll>> = flowOf(emptyList())

        override fun observePayroll(id: Long): Flow<Payroll?> = flowOf(createdPayroll)

        override suspend fun createPayroll(payroll: Payroll): Long {
            createdPayroll = payroll
            return 42
        }
    }
}
