package com.sumit.payrollmanagement.payroll.di

import android.content.Context
import androidx.room.Room
import com.sumit.payrollmanagement.payroll.data.local.PayrollDao
import com.sumit.payrollmanagement.payroll.data.local.PayrollDatabase
import com.sumit.payrollmanagement.payroll.data.remote.MockPayrollApi
import com.sumit.payrollmanagement.payroll.data.remote.PayrollApi
import com.sumit.payrollmanagement.payroll.data.repository.PayrollRepositoryImpl
import com.sumit.payrollmanagement.payroll.domain.repository.PayrollRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PayrollDataModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PayrollDatabase =
        Room.databaseBuilder(
            context,
            PayrollDatabase::class.java,
            "payroll.db"
        ).build()

    @Provides
    fun providePayrollDao(database: PayrollDatabase): PayrollDao = database.payrollDao()

    @Provides
    fun provideClock(): Clock = Clock.systemDefaultZone()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class PayrollBindingsModule {
    @Binds
    @Singleton
    abstract fun bindPayrollRepository(implementation: PayrollRepositoryImpl): PayrollRepository

    @Binds
    @Singleton
    abstract fun bindPayrollApi(implementation: MockPayrollApi): PayrollApi
}
