package smarthome.library.datalibrary.store.listeners;

import smarthome.library.common.message.Message;

import java.util.List;

public interface MessageListener {

    void onMessagesReceived(List<Message> messages, boolean isInnerCall);

}
