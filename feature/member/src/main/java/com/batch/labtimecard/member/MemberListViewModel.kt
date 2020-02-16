package com.batch.labtimecard.member


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batch.labtimecard.data.TimeCardRepository
import com.batch.labtimecard.data.model.MemberData
import com.batch.labtimecard.member.usecase.LoginUseCase
import com.batch.labtimecard.member.usecase.MemberUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class MemberListViewModel(
    private val memberUseCase: MemberUseCase,
    private val loginUseCase: LoginUseCase,
    private val timeCardRepository: TimeCardRepository
) : ViewModel() {


    private val _members = MutableLiveData<List<MemberData>>()
    val members: LiveData<List<MemberData>> = _members
    private val _isLoggedIn = MutableLiveData<Pair<String?, Boolean>>()
    val isLoggedIn: LiveData<Pair<String?, Boolean>> = _isLoggedIn
    val isLoading = MutableLiveData<Boolean>()

    val isRefreshing = MutableLiveData<Boolean>()

    private val _updatingMembers = MutableLiveData<Boolean>()
    val updatingMembers: LiveData<Boolean> = _updatingMembers

    fun fetchFromRemote() {
        isLoading.value = true
        viewModelScope.launch {
            timeCardRepository.fetchMembers()
                .collect {
                    _members.value = it
                    isLoading.value = false
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

    fun updateAllMember() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                _updatingMembers.value = true
                memberUseCase.updateYear(this)
                isLoading.value = false
                _updatingMembers.value = false
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun refresh() {
        val memberList = members.value ?: return
        viewModelScope.launch {
            runCatching { memberUseCase.refreshMembers(this, memberList) }
                .onFailure { Timber.e(it) }
                .also { isRefreshing.value = false }
        }

    }
}