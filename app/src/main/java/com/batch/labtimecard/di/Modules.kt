package com.batch.labtimecard.di

import com.batch.labtimecard.common.navigator.Navigator
import com.batch.labtimecard.navigator.NavigatorImpl
import org.koin.dsl.module

object Modules {
    val navigatorModule = module {
        single<Navigator> { NavigatorImpl() }
    }
}