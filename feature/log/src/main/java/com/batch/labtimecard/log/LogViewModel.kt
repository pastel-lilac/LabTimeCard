package com.batch.labtimecard.log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batch.labtimecard.data.TimeCardRepository
import com.batch.labtimecard.data.model.LoginLog
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import timber.log.Timber
import java.util.*

class LogViewModel(
    private val timeCardRepository: TimeCardRepository
) : ViewModel() {


    private val _logs = MutableLiveData<List<LoginLog>>()
    val logs: LiveData<List<LoginLog>> = _logs

    var selectedDate: LocalDate? = null

    var memberKey: String? = null

    fun fetchLogByMonth(month: Date) {
        viewModelScope.launch {
            val key = memberKey ?: return@launch
            runCatching { timeCardRepository.fetchLog(key, month) }
                .onSuccess { _logs.value = it }
                .onFailure { Timber.e(it) }
        }
    }

}