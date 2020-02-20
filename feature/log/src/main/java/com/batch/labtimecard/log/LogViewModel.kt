package com.batch.labtimecard.log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batch.labtimecard.data.TimeCardRepository
import com.batch.labtimecard.data.model.LogTime
import com.batch.labtimecard.data.model.LoginLog
import com.batch.labtimecard.data.model.MemberData
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import timber.log.Timber
import java.util.*

class LogViewModel(
    private val timeCardRepository: TimeCardRepository
) : ViewModel() {


    private val _logs = MutableLiveData<List<LoginLog>>()
    val logs: LiveData<List<LoginLog>> = _logs

    val events = mutableMapOf<LocalDate, LogTime>()

    var selectedDate: LocalDate? = null

    var member: MemberData? = null

    fun fetchLogByMonth(month: Date) {
        viewModelScope.launch {
            val key = member?.key ?: return@launch
            runCatching { timeCardRepository.fetchLog(key, month) }
                .onSuccess { _logs.value = it }
                .onFailure { Timber.e(it) }
        }
    }

}