package smarthome.library.datalibrary.store.listeners;

import java.util.List;

public interface MessageListener {

    void onMessagesReceived(List<Object> messages, boolean isInnerCall);

}
