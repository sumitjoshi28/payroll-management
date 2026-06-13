package com.sumit.payrollmanagement.payroll.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.payrollmanagement.payroll.domain.usecase.ObservePayrollsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PayrollListViewModel @Inject constructor(
    observePayrolls: ObservePayrollsUseCase
) : ViewModel() {
    val state: StateFlow<PayrollListUiState> = observePayrolls()
        .map { PayrollListUiState(isLoading = false, payrolls = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PayrollListUiState()
        )

    private val effectsChannel = Channel<PayrollListEffect>(Channel.BUFFERED)
    val effects = effectsChannel.receiveAsFlow()

    fun onAction(action: PayrollListAction) {
        viewModelScope.launch {
            when (action) {
                PayrollListAction.CreatePayroll -> {
                    effectsChannel.send(PayrollListEffect.NavigateToCreate)
                }
                is PayrollListAction.OpenPayroll -> {
                    effectsChannel.send(PayrollListEffect.NavigateToDetail(action.id))
                }
            }
        }
    }
}
