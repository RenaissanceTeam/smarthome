package smarthome.library.datalibrary.store

import smarthome.library.datalibrary.model.HomesReferences

interface HomesReferencesStorage {

    suspend fun replaceHomesReferences(homesReferences: HomesReferences)
    suspend fun mergeHomesReferences(updatedHomesReferences: HomesReferences)
    suspend fun addHomeReference(homeReference: String)
    suspend fun getHomesReferences(): HomesReferences
    suspend fun checkIfHomeExists(homeId: String): Boolean
}