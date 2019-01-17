package smarthome.datalibrary.database.store

import com.google.firebase.database.DatabaseReference

import smarthome.datalibrary.database.model.LinkedAccounts
import smarthome.datalibrary.database.store.listeners.LinkedAccountsListener

interface LinkedAccountsStorage {
    fun postLinkedAccounts(linkedAccounts: LinkedAccounts)
    fun postLinkedAccounts(linkedAccounts: LinkedAccounts, listener: DatabaseReference.CompletionListener)
    fun getLinkedAccounts(listener: LinkedAccountsListener)

}
