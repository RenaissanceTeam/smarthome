package smarthome.raspberry.util.di

import org.koin.dsl.module
import org.koin.experimental.builder.factory
import smarthome.raspberry.util.ResourceProvider
import smarthome.raspberry.util.persistence.PersistentStorage
import smarthome.raspberry.util.persistence.StorageHelper

private val innerModule = module {
    factory<ResourceProvider>()
    
    single<PersistentStorage> {
        TODO()
//        SharedPreferencesAdapter(
//            get<Context>().getSharedPreferences(
//                SharedPreferencesAdapter.PREFS_NAME,
//                Context.MODE_PRIVATE
//            )
//        )
    }
    factory<StorageHelper>()
    
}

val utilModule = listOf(innerModule)
