package smarthome.datalibrary.database.store.listeners

import smarthome.datalibrary.database.model.LinkedAccounts

interface LinkedAccountsListener {
    fun onLinkedAccountsReceived(linkedAccounts: LinkedAccounts)
}
