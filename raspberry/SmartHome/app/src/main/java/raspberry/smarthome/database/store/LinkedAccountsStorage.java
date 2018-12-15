package raspberry.smarthome.database.store;

import com.google.firebase.database.DatabaseReference;

import raspberry.smarthome.database.model.LinkedAccounts;
import raspberry.smarthome.database.store.listeners.LinkedAccountsListener;

public interface LinkedAccountsStorage {

    void postLinkedAccounts(LinkedAccounts linkedAccounts);

    void postLinkedAccounts(LinkedAccounts linkedAccounts, DatabaseReference.CompletionListener listener);

    void getLinkedAccounts(LinkedAccountsListener listener);

}
