package com.batch.labtimecard.member


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batch.labtimecard.data.TimeCardRepository
import com.batch.labtimecard.data.model.MemberData
import com.batch.labtimecard.member.usecase.LoginUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class MemberListViewModel(
    private val loginUseCase: LoginUseCase,
    private val timeCardRepository: TimeCardRepository
) : ViewModel() {


    private val _members = MutableLiveData<List<MemberData>>()
    val members: LiveData<List<MemberData>> = _members
    private val _isLoggedIn = MutableLiveData<Pair<String?, Boolean>>()
    val isLoggedIn: LiveData<Pair<String?, Boolean>> = _isLoggedIn

    fun fetchFromRemote() {
        viewModelScope.launch {
            timeCardRepository.fetchMembers()
                .collect {
                    _members.value = it
                }
        }
    }

    fun updateLoginState(item: MemberData) {
        viewModelScope.launch {
            runCatching { loginUseCase.updateLoginState(item) }
                .onSuccess {
                    _isLoggedIn.value = Pair(item.member?.name, it)
                }
                .onFailure { Timber.e(it) }
        }

    }
}