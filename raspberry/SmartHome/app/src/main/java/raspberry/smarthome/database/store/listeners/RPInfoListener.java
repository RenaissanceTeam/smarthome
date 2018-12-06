package raspberry.smarthome.database.store.listeners;

import raspberry.smarthome.database.model.RaspberryInfo;

public interface RPInfoListener {

    void onRaspberryInfoReceived(RaspberryInfo info);

}
