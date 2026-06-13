package com.sumit.payrollmanagement.payroll.presentation.common

import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Long.formatCurrency(): String = NumberFormat.getCurrencyInstance(Locale.US).format(this / 100.0)

fun Long.formatPayrollDate(): String = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
    .format(DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a"))
