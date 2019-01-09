package ru.smarthome.database.store

import com.google.firebase.database.DatabaseReference

import ru.smarthome.database.model.LinkedAccounts
import ru.smarthome.database.store.listeners.LinkedAccountsListener

interface LinkedAccountsStorage {
    fun postLinkedAccounts(linkedAccounts: LinkedAccounts)
    fun postLinkedAccounts(linkedAccounts: LinkedAccounts, listener: DatabaseReference.CompletionListener)
    fun getLinkedAccounts(listener: LinkedAccountsListener)

}
