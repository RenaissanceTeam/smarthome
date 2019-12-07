package smarthome.raspberry.util.di

import org.koin.dsl.module
import org.koin.experimental.builder.factory
import smarthome.raspberry.util.ResourceProvider
import smarthome.raspberry.util.SharedPreferencesHelper

private val innerModule = module {
    factory<ResourceProvider>()
    factory<SharedPreferencesHelper>()
}

val utilModule = listOf(innerModule)
