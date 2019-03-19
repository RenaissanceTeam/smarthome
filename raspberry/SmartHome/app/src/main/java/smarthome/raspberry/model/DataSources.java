package smarthome.raspberry.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import smarthome.library.common.BaseController;
import smarthome.library.common.IotDevice;
import smarthome.raspberry.arduinodevices.ArduinoDevice;
import smarthome.library.common.storage.JsonDataSource;
import smarthome.raspberry.arduinodevices.controllers.ArduinoController;
import smarthome.raspberry.arduinodevices.controllers.ArduinoDigitalAlert;
import smarthome.raspberry.arduinodevices.controllers.ArduinoHumidity;
import smarthome.raspberry.arduinodevices.controllers.ArduinoInit;
import smarthome.raspberry.arduinodevices.controllers.ArduinoOnOff;
import smarthome.raspberry.arduinodevices.controllers.ArduinoReadFloat;
import smarthome.raspberry.arduinodevices.controllers.ArduinoTemperature;

public enum DataSources {
    // todo add here third party device data sources, devices will be added after calling addDevice() in SH_repository
    // example:
    // XIAOMI(XiaomiDevice.class); where XiaomiDevice extends IotDevice
    ARDUINO(ArduinoDevice.class,
            ArduinoDigitalAlert.class, ArduinoHumidity.class, ArduinoInit.class, ArduinoOnOff.class,
            ArduinoReadFloat.class, ArduinoTemperature.class);

    Class<? extends IotDevice> deviceType;
    List<Class<? extends BaseController>> controllerType;
    JsonDataSource source;

    DataSources(Class<? extends IotDevice> deviceType,
                Class<? extends BaseController>... controllerType) {
        this.deviceType = deviceType;
        this.controllerType = Arrays.asList(controllerType);
    }

    void init(Context context) {
        source = new JsonDataSource(context.getApplicationContext(), deviceType, controllerType);
    }
}
