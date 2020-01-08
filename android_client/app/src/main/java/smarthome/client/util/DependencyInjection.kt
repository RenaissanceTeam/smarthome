package smarthome.client.util

import org.koin.dsl.module
import smarthome.client.domain.usecases.*

val usecasesModule = module {
    single { AuthenticationUseCaseImpl(get()) }
    single { CloudMessageUseCaseImpl(get()) }
    single { ControllersUseCaseImpl(get()) }
    single { DevicesUseCaseImpl(get()) }
}