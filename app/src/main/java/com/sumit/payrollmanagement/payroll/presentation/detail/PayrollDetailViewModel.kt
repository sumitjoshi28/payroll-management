package com.sumit.payrollmanagement.payroll.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumit.payrollmanagement.payroll.domain.usecase.ObservePayrollUseCase
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
class PayrollDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observePayroll: ObservePayrollUseCase
) : ViewModel() {
    private val payrollId: Long = checkNotNull(savedStateHandle[PAYROLL_ID_ARGUMENT])

    val state: StateFlow<PayrollDetailUiState> = observePayroll(payrollId)
        .map { PayrollDetailUiState(isLoading = false, payroll = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PayrollDetailUiState()
        )

    private val effectsChannel = Channel<PayrollDetailEffect>(Channel.BUFFERED)
    val effects = effectsChannel.receiveAsFlow()

    fun onAction(action: PayrollDetailAction) {
        when (action) {
            PayrollDetailAction.Back -> viewModelScope.launch {
                effectsChannel.send(PayrollDetailEffect.NavigateBack)
            }
        }
    }

    companion object {
        const val PAYROLL_ID_ARGUMENT = "payrollId"
    }
}
