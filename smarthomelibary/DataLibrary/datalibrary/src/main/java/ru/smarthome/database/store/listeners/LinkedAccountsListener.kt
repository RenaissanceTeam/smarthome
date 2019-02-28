package ru.smarthome.database.store.listeners

import ru.smarthome.database.model.LinkedAccounts

interface LinkedAccountsListener {
    fun onLinkedAccountsReceived(linkedAccounts: LinkedAccounts)
}
