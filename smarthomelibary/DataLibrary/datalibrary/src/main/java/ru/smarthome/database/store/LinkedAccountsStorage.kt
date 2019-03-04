package ru.smarthome.database.store

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import ru.smarthome.database.constants.*

import ru.smarthome.database.model.LinkedAccounts
import ru.smarthome.database.store.listeners.LinkedAccountsListener

interface LinkedAccountsStorage {
    fun postLinkedAccounts(
        linkedAccounts: LinkedAccounts,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun getLinkedAccounts(listener: LinkedAccountsListener)
}
