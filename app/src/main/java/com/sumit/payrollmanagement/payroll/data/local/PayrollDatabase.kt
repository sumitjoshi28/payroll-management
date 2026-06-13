package com.sumit.payrollmanagement.payroll.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PayrollEntity::class, EmployeeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PayrollDatabase : RoomDatabase() {
    abstract fun payrollDao(): PayrollDao
}
