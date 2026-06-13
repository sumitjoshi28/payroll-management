package com.sumit.payrollmanagement.payroll.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "payrolls")
data class PayrollEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val createdAtEpochMillis: Long
)

@Entity(
    tableName = "employees",
    foreignKeys = [
        ForeignKey(
            entity = PayrollEntity::class,
            parentColumns = ["id"],
            childColumns = ["payrollId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("payrollId")]
)
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val payrollId: Long,
    val position: Int,
    val name: String,
    val wagesCents: Long,
    val isExempt: Boolean,
    val taxCents: Long
)

data class PayrollWithEmployees(
    @Embedded val payroll: PayrollEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "payrollId"
    )
    val employees: List<EmployeeEntity>
)
