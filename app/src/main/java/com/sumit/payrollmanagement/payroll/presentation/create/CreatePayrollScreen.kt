package com.sumit.payrollmanagement.payroll.presentation.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CreatePayrollRoute(
    onBack: () -> Unit,
    onPayrollCreated: (Long) -> Unit,
    viewModel: CreatePayrollViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                CreatePayrollEffect.NavigateBack -> onBack()
                is CreatePayrollEffect.PayrollCreated -> onPayrollCreated(effect.payrollId)
            }
        }
    }

    CreatePayrollScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePayrollScreen(
    state: CreatePayrollUiState,
    onAction: (CreatePayrollAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create payroll") },
                navigationIcon = {
                    TextButton(onClick = { onAction(CreatePayrollAction.Back) }) {
                        Text("Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = { onAction(CreatePayrollAction.Submit) },
                    enabled = !state.isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(52.dp)
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Create payroll")
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "Employees",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Add wages and mark employees who are exempt from tax.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            itemsIndexed(
                items = state.employees,
                key = { _, employee -> employee.formId }
            ) { index, employee ->
                EmployeeFormCard(
                    index = index,
                    employee = employee,
                    canRemove = state.employees.size > 1,
                    onAction = onAction
                )
            }

            item {
                OutlinedButton(
                    onClick = { onAction(CreatePayrollAction.AddEmployee) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("+ Add employee")
                }
            }

            state.errorMessage?.let { errorMessage ->
                item {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun EmployeeFormCard(
    index: Int,
    employee: EmployeeFormState,
    canRemove: Boolean,
    onAction: (CreatePayrollAction) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Employee ${index + 1}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (canRemove) {
                    TextButton(
                        onClick = { onAction(CreatePayrollAction.RemoveEmployee(employee.formId)) }
                    ) {
                        Text("Remove", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = employee.name,
                onValueChange = {
                    onAction(CreatePayrollAction.NameChanged(employee.formId, it))
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Full name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = employee.wages,
                onValueChange = {
                    onAction(CreatePayrollAction.WagesChanged(employee.formId, it))
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Wages") },
                prefix = { Text("$") },
                supportingText = { Text("5% tax applies above $1,000 unless exempt") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Tax exempt", style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = "No taxes will be deducted",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = employee.isExempt,
                    onCheckedChange = {
                        onAction(CreatePayrollAction.ExemptChanged(employee.formId, it))
                    }
                )
            }
        }
    }
}
