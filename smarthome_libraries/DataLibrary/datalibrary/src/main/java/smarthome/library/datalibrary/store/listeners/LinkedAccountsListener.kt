package smarthome.library.datalibrary.store.listeners

import smarthome.library.datalibrary.model.LinkedAccounts

interface LinkedAccountsListener {
    fun onLinkedAccountsReceived(linkedAccounts: LinkedAccounts)
}
