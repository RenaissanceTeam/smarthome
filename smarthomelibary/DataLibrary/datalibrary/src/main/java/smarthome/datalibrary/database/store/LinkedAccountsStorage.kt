package smarthome.datalibrary.database.store

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import smarthome.datalibrary.database.constants.Constants.defFailureListener
import smarthome.datalibrary.database.constants.Constants.defSuccessListener

import smarthome.datalibrary.database.model.LinkedAccounts
import smarthome.datalibrary.database.store.listeners.LinkedAccountsListener

interface LinkedAccountsStorage {
    fun postLinkedAccounts(linkedAccounts: LinkedAccounts, successListener: OnSuccessListener<Void> = defSuccessListener, failureListener: OnFailureListener = defFailureListener)
    fun getLinkedAccounts(listener: LinkedAccountsListener)
}
