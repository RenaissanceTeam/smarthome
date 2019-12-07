package smarthome.raspberry.external.datalibrary

import org.koin.core.KoinComponent
import smarthome.library.common.HomesReferencesStorage
import smarthome.library.common.SmartHomeStorage
import smarthome.library.datalibrary.FirestoreHomesReferencesStorage
import smarthome.library.datalibrary.FirestoreSmartHomeStorage

object DatalibraryAdapter : KoinComponent {
    fun provideSmartHomeStorage(): SmartHomeStorage {
        return FirestoreSmartHomeStorage("") // todo - how to get at inject-time valid homeId?
    }
    fun provideHomesReferencesStorage(): HomesReferencesStorage {
        return FirestoreHomesReferencesStorage("") // todo - how to get uid at inject-time
    }
}