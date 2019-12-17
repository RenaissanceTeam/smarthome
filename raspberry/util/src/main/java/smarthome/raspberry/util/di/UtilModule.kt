package smarthome.raspberry.util.di

import android.content.Context
import org.koin.dsl.module
import org.koin.experimental.builder.factory
import smarthome.raspberry.util.ResourceProvider
import smarthome.raspberry.util.persistence.PersistentStorage
import smarthome.raspberry.util.persistence.StorageHelper
import smarthome.raspberry.util.persistence.sharedprefs.SharedPreferencesAdapter

private val innerModule = module {
    factory<ResourceProvider>()
    
    single<PersistentStorage> {
        SharedPreferencesAdapter(
            get<Context>().getSharedPreferences(
                SharedPreferencesAdapter.PREFS_NAME,
                Context.MODE_PRIVATE
            )
        )
    }
    factory<StorageHelper>()
    
}

val utilModule = listOf(innerModule)
