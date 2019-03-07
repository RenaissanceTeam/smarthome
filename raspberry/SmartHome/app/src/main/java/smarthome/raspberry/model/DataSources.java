package smarthome.raspberry.model;

import android.content.Context;

import smarthome.library.common.IotDevice;
import smarthome.raspberry.arduinodevices.ArduinoDevice;
import smarthome.library.common.storage.JsonDataSource;

public enum DataSources {
    // todo add here third party device data sources, devices will be added after calling addDevice() in SH_repository
    // example:
    // XIAOMI(XiaomiDevice.class); where XiaomiDevice extends IotDevice
    ARDUINO(ArduinoDevice.class);

    Class<? extends IotDevice> type;
    JsonDataSource source;

    DataSources(Class<? extends IotDevice> storedDataType) {
        this.type = storedDataType;
    }

    void init(Context context) {
        source = new JsonDataSource(context.getApplicationContext(), type);
    }
}
