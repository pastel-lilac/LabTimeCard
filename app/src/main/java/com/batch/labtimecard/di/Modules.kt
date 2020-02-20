package com.batch.labtimecard.di

import com.batch.labtimecard.common.navigator.Navigator
import com.batch.labtimecard.data.TimeCardRepository
import com.batch.labtimecard.data.TimeCardRepositoryImpl
import com.batch.labtimecard.data.api.BASE_URL
import com.batch.labtimecard.log.LogViewModel
import com.batch.labtimecard.member.MemberListViewModel
import com.batch.labtimecard.member.usecase.LoginUseCase
import com.batch.labtimecard.member.usecase.LoginUseCaseImpl
import com.batch.labtimecard.member.usecase.MemberUseCase
import com.batch.labtimecard.member.usecase.MemberUseCaseImpl
import com.batch.labtimecard.navigator.NavigatorImpl
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object Modules {
    val apiModule = module {
        single {
            OkHttpClient()
                .newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(StethoInterceptor())
                .build()
        }

        single {
            Moshi
                .Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }


        single {
            Retrofit.Builder()
                .client(get())
                .addConverterFactory(MoshiConverterFactory.create(get()))
                .baseUrl(BASE_URL)
                .build()
        }
    }

    val navigatorModule = module {
        single<Navigator> { NavigatorImpl() }
    }

    val repositoryModule = module {
        single<TimeCardRepository> { TimeCardRepositoryImpl(get()) }
    }

    val useCaseModule = module {
        single<LoginUseCase> { LoginUseCaseImpl(get()) }
        single<MemberUseCase> { MemberUseCaseImpl(get()) }
    }

    val viewModelModule = module {
        viewModel { MemberListViewModel(get(), get(), get()) }
        viewModel { LogViewModel(get()) }
    }

}