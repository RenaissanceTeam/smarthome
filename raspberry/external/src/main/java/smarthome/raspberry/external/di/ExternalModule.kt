package smarthome.raspberry.external.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import smarthome.library.datalibrary.api.SmartHomeStorage
import smarthome.library.datalibrary.FirestoreSmartHomeStorage
import smarthome.raspberry.external.datalibrary.DatalibraryAdapter

val externalModule = listOf(
        module {
            
            scope(named("homeId")) {
                scoped<smarthome.library.datalibrary.api.SmartHomeStorage> {
                    FirestoreSmartHomeStorage(get(named("homeId")))
                }
            }
            
            factory { DatalibraryAdapter.provideHomesReferencesStorage()}
        }
)