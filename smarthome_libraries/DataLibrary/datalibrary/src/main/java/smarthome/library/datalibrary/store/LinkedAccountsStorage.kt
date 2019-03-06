package smarthome.library.datalibrary.store

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import smarthome.library.datalibrary.constants.defFailureListener
import smarthome.library.datalibrary.constants.defSuccessListener

import smarthome.library.datalibrary.model.LinkedAccounts
import smarthome.library.datalibrary.store.listeners.LinkedAccountsListener

interface LinkedAccountsStorage {
    fun postLinkedAccounts(
        linkedAccounts: LinkedAccounts,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun getLinkedAccounts(listener: LinkedAccountsListener)
}
