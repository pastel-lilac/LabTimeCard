package com.github.pastel_lilac.register

import android.widget.RadioGroup
import androidx.lifecycle.*
import com.batch.labtimecard.data.TimeCardRepository
import com.batch.labtimecard.data.model.Member
import kotlinx.coroutines.launch
import timber.log.Timber

class RegisterUserViewModel(
    private val timeCardRepository: TimeCardRepository
) : ViewModel() {

    val name = MutableLiveData<String>()

    private val _isRadioButtonChecked = MutableLiveData<Boolean>()

    val registerButtonEnabled = MediatorLiveData<Boolean>()

    private val _isRegisterSucceeded = MutableLiveData<Boolean>()
    val isRegisterSucceeded: LiveData<Boolean> = _isRegisterSucceeded


    init {
        _isRadioButtonChecked.value = false
        registerButtonEnabled.value = false
        val observer = Observer<Any> {
            if (name.value.isNullOrEmpty()) {
                registerButtonEnabled.value = false
                return@Observer
            }
            val isChecked = _isRadioButtonChecked.value ?: false
            if (!isChecked) return@Observer
            registerButtonEnabled.value = true
        }
        registerButtonEnabled.apply {
            addSource(name, observer)
            addSource(_isRadioButtonChecked, observer)
        }

    }

    fun registerUser(member: Member) {
        viewModelScope.launch {
            runCatching { timeCardRepository.registerMember(member) }
                .onSuccess {
                    _isRegisterSucceeded.value = true
                }
                .onFailure {
                    _isRegisterSucceeded.value = false
                    Timber.e(it)
                }
        }
    }

    fun onSplitTypeChanged(group: RadioGroup, id: Int) {
        _isRadioButtonChecked.value = true
    }
}