package smarthome.client.util

import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module
import smarhome.client.data.*
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
    val homeRepo = HomeRepositoryImpl()
    single { homeRepo as HomeRepository }
    single { homeRepo as RemoteStorageInput }
    single { AuthenticationRepositoryImpl(get()) as AuthenticationRepository }
    single { FirebaseAuth.getInstance() }
}

val dataSourceModule = module {
    single { LocalStorageImpl(get()) as LocalStorage }
    single { RemoteStorageImpl() as RemoteStorage }
}