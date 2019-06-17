package smarthome.client.util

import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module
import smarthome.client.data.*
import smarthome.client.domain.AuthenticationRepository
import smarthome.client.domain.HomeRepository
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
val repositoryModule = module {
    single { HomeRepositoryImpl(get(), get()) as HomeRepository}
    single { AuthenticationRepositoryImpl(get()) as AuthenticationRepository}
    single { FirebaseAuth.getInstance() }
}

val dataSourceModule = module {
    single { LocalStorageImpl(get()) as LocalStorage }
    single { RemoteStorageImpl() as RemoteStorage }
}