package smarthome.raspberry.model;

import android.content.Context;

import smarthome.library.common.BaseController;
import smarthome.library.common.IotDevice;
import smarthome.raspberry.arduinodevices.ArduinoDevice;
import smarthome.library.common.storage.JsonDataSource;
import smarthome.raspberry.arduinodevices.controllers.ArduinoController;

public enum DataSources {
    // todo add here third party device data sources, devices will be added after calling addDevice() in SH_repository
    // example:
    // XIAOMI(XiaomiDevice.class); where XiaomiDevice extends IotDevice
    ARDUINO(ArduinoDevice.class, ArduinoController.class);

    Class<? extends IotDevice> deviceType;
    Class<? extends BaseController> controllerType;
    JsonDataSource source;

    DataSources(Class<? extends IotDevice> deviceType,
                Class<? extends BaseController> controllerType) {
        this.deviceType = deviceType;
        this.controllerType = controllerType;
    }

    void init(Context context) {
        source = new JsonDataSource(context.getApplicationContext(), deviceType, controllerType);
    }
}
