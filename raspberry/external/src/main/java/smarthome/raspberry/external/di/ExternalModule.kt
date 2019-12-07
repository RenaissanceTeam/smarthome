package smarthome.raspberry.external.di

import org.koin.dsl.module
import smarthome.raspberry.external.datalibrary.DatalibraryAdapter

val externalModule = listOf(
        module {
            factory { DatalibraryAdapter.provideSmartHomeStorage() }
            factory { DatalibraryAdapter.provideHomesReferencesStorage()}
        }
)