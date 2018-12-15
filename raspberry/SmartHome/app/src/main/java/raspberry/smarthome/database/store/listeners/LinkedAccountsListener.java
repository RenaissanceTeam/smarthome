package raspberry.smarthome.database.store.listeners;

import raspberry.smarthome.database.model.LinkedAccounts;

public interface LinkedAccountsListener {

    void onLinkedAccountsReceived(LinkedAccounts linkedAccounts);

}
