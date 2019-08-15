package smarthome.client.util

import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module
import smarthome.client.data.*
import smarthome.client.domain.domain.AuthenticationRepository
import smarthome.client.domain.domain.HomeRepository
import smarthome.client.domain.usecases.*

val usecasesModule = module {
    single { smarthome.client.domain.domain.usecases.AuthenticationUseCase(get()) }
    single { smarthome.client.domain.domain.usecases.CloudMessageUseCase(get()) }
    single { smarthome.client.domain.domain.usecases.ControllersUseCase(get()) }
    single { smarthome.client.domain.domain.usecases.DevicesUseCase(get()) }
    single { smarthome.client.domain.domain.usecases.HomeUseCases(get()) }
    single { smarthome.client.domain.domain.usecases.PendingControllersUseCase(get()) }
    single { smarthome.client.domain.domain.usecases.PendingDevicesUseCase(get()) }
    single { smarthome.client.domain.domain.usecases.ScriptUseCase(get()) }
}
val repositoryModule = module {
    val homeRepo = smarhome.client.data.data.HomeRepositoryImpl()
    single { homeRepo as smarthome.client.domain.domain.HomeRepository }
    single { homeRepo as smarhome.client.data.data.RemoteStorageInput }
    single { smarhome.client.data.data.AuthenticationRepositoryImpl(get()) as smarthome.client.domain.domain.AuthenticationRepository }
    single { FirebaseAuth.getInstance() }
}

val dataSourceModule = module {
    single { smarhome.client.data.data.LocalStorageImpl(
            get()) as smarhome.client.data.data.LocalStorage
    }
    single { smarhome.client.data.data.RemoteStorageImpl() as smarhome.client.data.data.RemoteStorage }
}