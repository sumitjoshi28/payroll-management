package com.sumit.payrollmanagement.payroll.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sumit.payrollmanagement.payroll.presentation.create.CreatePayrollRoute
import com.sumit.payrollmanagement.payroll.presentation.detail.PayrollDetailRoute
import com.sumit.payrollmanagement.payroll.presentation.detail.PayrollDetailViewModel
import com.sumit.payrollmanagement.payroll.presentation.list.PayrollListRoute

@Composable
fun PayrollApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PayrollDestination.List.route
    ) {
        composable(PayrollDestination.List.route) {
            PayrollListRoute(
                onCreatePayroll = { navController.navigate(PayrollDestination.Create.route) },
                onOpenPayroll = { payrollId ->
                    navController.navigate(PayrollDestination.Detail.createRoute(payrollId))
                }
            )
        }
        composable(PayrollDestination.Create.route) {
            CreatePayrollRoute(
                onBack = navController::popBackStack,
                onPayrollCreated = { payrollId ->
                    navController.navigate(PayrollDestination.Detail.createRoute(payrollId)) {
                        popUpTo(PayrollDestination.List.route)
                    }
                }
            )
        }
        composable(
            route = PayrollDestination.Detail.route,
            arguments = listOf(
                navArgument(PayrollDetailViewModel.PAYROLL_ID_ARGUMENT) {
                    type = NavType.LongType
                }
            )
        ) {
            PayrollDetailRoute(
                onBack = navController::popBackStack,
                viewModel = hiltViewModel()
            )
        }
    }
}

private sealed class PayrollDestination(val route: String) {
    data object List : PayrollDestination("payrolls")
    data object Create : PayrollDestination("payrolls/create")
    data object Detail : PayrollDestination("payrolls/{${PayrollDetailViewModel.PAYROLL_ID_ARGUMENT}}") {
        fun createRoute(payrollId: Long): String = "payrolls/$payrollId"
    }
}
