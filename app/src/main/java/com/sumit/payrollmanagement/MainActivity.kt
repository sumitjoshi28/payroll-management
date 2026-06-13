package com.sumit.payrollmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sumit.payrollmanagement.payroll.presentation.navigation.PayrollApp
import com.sumit.payrollmanagement.ui.theme.PayrollManagementTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PayrollManagementTheme {
                PayrollApp()
            }
        }
    }
}
