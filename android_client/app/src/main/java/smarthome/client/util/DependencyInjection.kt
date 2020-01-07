package smarthome.client.util

import org.koin.dsl.module
import smarthome.client.domain.usecases.*

val usecasesModule = module {
    single { AuthenticationUseCase(get()) }
    single { CloudMessageUseCase(get()) }
    single { ControllersUseCase(get()) }
    single { DevicesUseCase(get()) }
    single { HomeUseCases(get()) }
    single { PendingControllersUseCase(get()) }
    single { PendingDevicesUseCase(get()) }
    single { ScriptUseCase(get()) }
}