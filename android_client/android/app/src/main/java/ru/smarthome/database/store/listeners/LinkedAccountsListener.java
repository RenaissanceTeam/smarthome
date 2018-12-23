package ru.smarthome.database.store.listeners;

import ru.smarthome.database.model.LinkedAccounts;

public interface LinkedAccountsListener {

    void onLinkedAccountsReceived(LinkedAccounts linkedAccounts);

}
