package smarthome.client.util

import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module
import smarhome.client.data.*
import smarthome.client.data_api.*
import smarthome.client.domain.usecases.*
import smarthome.library.datalibrary.FirestoreHomesReferencesStorage
import smarthome.library.datalibrary.FirestoreInstanceTokenStorage
import smarthome.library.datalibrary.FirestoreSmartHomeStorage

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
    single {
        RemoteStorageImpl({ FirestoreInstanceTokenStorage(it)  },
                { FirestoreHomesReferencesStorage(it) },
                { FirestoreSmartHomeStorage(it) }) as RemoteStorage
    }
}