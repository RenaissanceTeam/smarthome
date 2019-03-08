package smarthome.raspberry.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import smarthome.raspberry.MainActivity;
import smarthome.library.common.storage.JsonDataSource;
import smarthome.raspberry.arduinodevices.ArduinoDevice;
import smarthome.library.common.BaseController;
import smarthome.library.common.GUID;
import smarthome.library.common.IotDevice;
import smarthome.library.common.SmartHome;

import static smarthome.raspberry.MainActivity.DEBUG;

public class SmartHomeRepository extends SmartHome {
    public static final String TAG = SmartHomeRepository.class.getSimpleName();
    private static SmartHomeRepository sInstance;
    private Context context;

    private SmartHomeRepository() {
    }

    public static SmartHomeRepository getInstance() {
        if (sInstance == null) {
            sInstance = new SmartHomeRepository();
            sInstance.devices = HomeStateStorage.get();
        }
        return sInstance;
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
        devices = new ArrayList<>();

        for (DataSources dataSource : DataSources.values()) {
            dataSource.init(this.context);
            devices.addAll(dataSource.source.getAll());
        }
    }

    /**
     * Main entry point for adding devices, call this method, when new
     * Iot device should be added to databases
     */
    public boolean addDevice(final IotDevice device) {
        if (DEBUG) Log.d(TAG, "addDevice: " + device);

        for (DataSources dataSource : DataSources.values()) {
            if (dataSource.type.equals(device.getClass())) {

                if (dataSource.source.contains(device)) {
                    dataSource.source.update(device);
                    devices.set(devices.indexOf(device), device);
                    return true;
                }

               boolean wasAdded = dataSource.source.add(device);
               if (wasAdded) {
                   // todo notify every sub about new device added
                   // todo set alarms for auto refresh for each controller

                   devices.add(device);
                   return true;
               }
            }
        }
        return false;
    }

    public BaseController getController(long guid) {
        for (IotDevice device : devices) {
            for (BaseController controller : device.controllers) {
                if (controller.guid == guid) {
                    return controller;
                }
            }
        }
        throw new IllegalArgumentException("No controller with guid=" + guid);
    }

    public ArduinoDevice getArduinoByIp(String ip) {
        for (IotDevice device : devices) {
            if (device instanceof ArduinoDevice) {
                if (((ArduinoDevice) device).ip.equals(ip)) {
                    return (ArduinoDevice) device;
                }
            }
        }
        throw new IllegalArgumentException("No device with ip=" + ip);
    }

    public void removeAll() {
        for (DataSources dataSource : DataSources.values()) {
            dataSource.source.clearAll();
        }

        for (IotDevice device : devices) {
            for (BaseController controller : device.controllers) {
                GUID.getInstance().remove(controller.guid);
            }
            GUID.getInstance().remove(device.guid);
        }
        devices.clear();
    }
}
