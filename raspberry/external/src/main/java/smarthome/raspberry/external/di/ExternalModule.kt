package smarthome.raspberry.external.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import smarthome.library.common.SmartHomeStorage
import smarthome.library.datalibrary.FirestoreSmartHomeStorage
import smarthome.raspberry.external.datalibrary.DatalibraryAdapter

val externalModule = listOf(
        module {
            
            scope(named("homeId")) {
                scoped<SmartHomeStorage> {
                    FirestoreSmartHomeStorage(get(named("homeId")))
                }
            }
            
            factory { DatalibraryAdapter.provideHomesReferencesStorage()}
        }
)