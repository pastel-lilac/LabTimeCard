package com.batch.labtimecard.di

import com.batch.labtimecard.common.navigator.Navigator
import com.batch.labtimecard.data.TimeCardRepository
import com.batch.labtimecard.data.TimeCardRepositoryImpl
import com.batch.labtimecard.member.MemberListViewModel
import com.batch.labtimecard.member.usecase.LoginUseCase
import com.batch.labtimecard.member.usecase.LoginUseCaseImpl
import com.batch.labtimecard.navigator.NavigatorImpl
import com.github.pastel_lilac.register.RegisterUserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {
    val navigatorModule = module {
        single<Navigator> { NavigatorImpl() }
    }

    val repositoryModule = module {
        single<TimeCardRepository> { TimeCardRepositoryImpl() }
    }

    val useCaseModule = module {
        single<LoginUseCase> { LoginUseCaseImpl(get()) }
    }

    val viewModelModule = module {
        viewModel { MemberListViewModel(get(), get()) }
        viewModel { RegisterUserViewModel(get()) }
    }

}