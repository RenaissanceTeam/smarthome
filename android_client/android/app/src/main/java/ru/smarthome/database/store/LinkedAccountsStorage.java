package ru.smarthome.database.store;

import com.google.firebase.database.DatabaseReference;

import ru.smarthome.database.model.LinkedAccounts;
import ru.smarthome.database.store.listeners.LinkedAccountsListener;

public interface LinkedAccountsStorage {

    void postLinkedAccounts(LinkedAccounts linkedAccounts);

    void postLinkedAccounts(LinkedAccounts linkedAccounts, DatabaseReference.CompletionListener listener);

    void getLinkedAccounts(LinkedAccountsListener listener);

}
