package smarthome.library.datalibrary.store.listeners

import smarthome.library.datalibrary.model.HomesReferences

interface HomesReferencesListener {
    fun onHomesReferencesReceived(homesReferences: HomesReferences)
}