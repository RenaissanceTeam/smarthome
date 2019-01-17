package smarthome.datalibrary.database.store

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference

import smarthome.datalibrary.database.model.LinkedAccounts
import smarthome.datalibrary.database.store.listeners.LinkedAccountsListener

interface LinkedAccountsStorage {
    fun postLinkedAccounts(linkedAccounts: LinkedAccounts)
    fun postLinkedAccounts(linkedAccounts: LinkedAccounts, listener: OnSuccessListener<Void>)
    fun getLinkedAccounts(listener: LinkedAccountsListener)

}
