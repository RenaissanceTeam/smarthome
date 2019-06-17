package smarthome.client.util

import org.koin.dsl.module
import smarthome.client.data.*
import smarthome.client.domain.HomeRepository

val appModule = module {
    single {  }
}

val repositoryModule = module {
    single { HomeRepositoryImpl(get(), get()) as HomeRepository}
}

val dataSourceModule = module {
    single { LocalStorageImpl(get()) as LocalStorage }
    single { RemoteStorageImpl() as RemoteStorage }
}