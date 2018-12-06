package raspberry.smarthome.database.store;

import com.google.firebase.database.DatabaseReference;

import raspberry.smarthome.database.model.RaspberryInfo;
import raspberry.smarthome.database.store.listeners.RPInfoListener;

public interface RPInfoStorage {

    void postRaspberryInfo(RaspberryInfo info);

    void postRaspberryInfo(RaspberryInfo info, DatabaseReference.CompletionListener listener);

    void getRaspberryInfo(RPInfoListener listener);

}
