package smarthome.raspberry.external.datalibrary

import org.koin.core.KoinComponent
import smarthome.library.datalibrary.api.HomesReferencesStorage
import smarthome.library.datalibrary.api.SmartHomeStorage
import smarthome.library.datalibrary.FirestoreHomesReferencesStorage
import smarthome.library.datalibrary.FirestoreSmartHomeStorage

object DatalibraryAdapter : KoinComponent {
    fun provideSmartHomeStorage(): smarthome.library.datalibrary.api.SmartHomeStorage {
        return FirestoreSmartHomeStorage("") // todo - how to get at inject-time valid homeId?
    }
    fun provideHomesReferencesStorage(): smarthome.library.datalibrary.api.HomesReferencesStorage {
        return FirestoreHomesReferencesStorage("") // todo - how to get uid at inject-time
    }
}