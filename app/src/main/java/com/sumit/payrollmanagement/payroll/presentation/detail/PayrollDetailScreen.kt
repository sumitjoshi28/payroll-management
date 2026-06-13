package com.sumit.payrollmanagement.payroll.presentation.detail

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sumit.payrollmanagement.payroll.domain.model.EmployeePay
import com.sumit.payrollmanagement.payroll.domain.model.Payroll
import com.sumit.payrollmanagement.payroll.presentation.common.formatCurrency
import com.sumit.payrollmanagement.payroll.presentation.common.formatPayrollDate

@Composable
fun PayrollDetailRoute(
    onBack: () -> Unit,
    viewModel: PayrollDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                PayrollDetailEffect.NavigateBack -> onBack()
            }
        }
    }

    PayrollDetailScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayrollDetailScreen(
    state: PayrollDetailUiState,
    onAction: (PayrollDetailAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payroll details") },
                navigationIcon = {
                    TextButton(onClick = { onAction(PayrollDetailAction.Back) }) {
                        Text("Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            state.isLoading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            state.payroll == null -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Payroll not found")
            }
            else -> PayrollDetailContent(
                payroll = state.payroll,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun PayrollDetailContent(
    payroll: Payroll,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = payroll.createdAtEpochMillis.formatPayrollDate(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${payroll.employees.size} ${if (payroll.employees.size == 1) "employee" else "employees"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
        }

        items(payroll.employees, key = EmployeePay::id) { employee ->
            EmployeePayCard(employee)
        }

        item {
            PayrollSummaryCard(employees = payroll.employees)
        }
    }
}

@Composable
private fun EmployeePayCard(employee: EmployeePay) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.surfaceContainerLow
            } else {
                MaterialTheme.colorScheme.surfaceContainerHigh
            }
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = employee.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                if (employee.isExempt) {
                    Text(
                        text = "Exempt",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            AmountRow("Total", employee.wagesCents.formatCurrency())
            AmountRow("Taxes", employee.taxCents.formatCurrency())
            HorizontalDivider(Modifier.padding(vertical = 10.dp))
            AmountRow(
                label = "Net",
                value = employee.netCents.formatCurrency(),
                emphasized = true
            )
        }
    }
}

@Composable
private fun PayrollSummaryCard(employees: List<EmployeePay>) {
    val totalWagesCents = employees.sumOf { employee -> employee.wagesCents }
    val totalTaxCents = employees.sumOf { employee -> employee.taxCents }
    val totalNetCents = employees.sumOf { employee -> employee.netCents }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(
                text = "Payroll summary",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
            AmountRow("Total", totalWagesCents.formatCurrency())
            if (totalTaxCents > 0) {
                AmountRow("Total Taxes", totalTaxCents.formatCurrency())
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
            )
            AmountRow(
                label = "Total Net",
                value = totalNetCents.formatCurrency(),
                emphasized = true
            )
        }
    }
}

@Composable
private fun AmountRow(
    label: String,
    value: String,
    emphasized: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (emphasized) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            fontWeight = if (emphasized) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            style = if (emphasized) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            fontWeight = if (emphasized) FontWeight.Bold else FontWeight.Medium
        )
    }
}
