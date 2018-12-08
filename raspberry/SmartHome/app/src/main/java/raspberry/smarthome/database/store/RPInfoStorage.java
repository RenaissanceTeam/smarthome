package raspberry.smarthome.database.store;

import com.google.firebase.database.DatabaseReference;

import raspberry.smarthome.database.store.listeners.RPInfoListener;

public interface RPInfoStorage {

    void postRaspberryIp(String ip);

    void postRaspberryIp(String ip, DatabaseReference.CompletionListener listener);

    void getRaspberryInfo(RPInfoListener listener);

}
