package smarthome.library.datalibrary.store

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import smarthome.library.datalibrary.constants.defFailureListener
import smarthome.library.datalibrary.constants.defSuccessListener
import smarthome.library.datalibrary.model.HomesReferences
import smarthome.library.datalibrary.store.listeners.HomeExistenceListener
import smarthome.library.datalibrary.store.listeners.HomesReferencesListener

interface HomesReferencesStorage {

    /**
     * posting new data with replacement
     */
    fun postHomesReferences(
        homesReferences: HomesReferences,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    /**
     * posting new data with merge
     */
    fun updateHomesReferences(
        updatedHomesReferences: HomesReferences,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun addHomeReference(
        homeReference: String,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun getHomesReferences(
        listener: HomesReferencesListener,
        failureListener: OnFailureListener = defFailureListener
        )

    fun checkIfHomeExists(
        homeId: String,
        listener: HomeExistenceListener,
        failureListener: OnFailureListener = defFailureListener
    )
}