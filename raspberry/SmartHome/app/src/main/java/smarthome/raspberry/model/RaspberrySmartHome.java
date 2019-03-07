package smarthome.raspberry.model;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import smarthome.raspberry.MainActivity;
import smarthome.raspberry.arduinodevices.ArduinoDevice;
import smarthome.raspberry.arduinodevices.controllers.ArduinoReadable;
import smarthome.library.common.BaseController;
import smarthome.library.common.GUID;
import smarthome.library.common.IotDevice;
import smarthome.library.common.SmartHome;

public class RaspberrySmartHome extends SmartHome {
    public static final String TAG = RaspberrySmartHome.class.getSimpleName();
    private static RaspberrySmartHome sInstance;

    private RaspberrySmartHome() {
    }

    public static RaspberrySmartHome getInstance() {
        if (sInstance == null) {
            sInstance = new RaspberrySmartHome();
            sInstance.devices = HomeStateStorage.get();
        }
        return sInstance;
    }

    public boolean addDevice(final IotDevice device) {
        if (MainActivity.DEBUG) Log.d(TAG, "addDevice: " + device);
        if (devices.contains(device)) return false;
        // todo decompose into pubsub, notify every sub about new device added
        Log.d(TAG, "run: startServer initial controllers reading");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                readEachController(device.controllers);
            }
        });

        // todo set alarms for auto refresh for each controller

        boolean wasAdded = devices.add(device);
        if (wasAdded) {
            HomeStateStorage.set(devices);
        }

        return wasAdded;
    }

    private void readEachController(List<BaseController> controllers) {
        for (BaseController controller : controllers) {
            if (controller instanceof ArduinoReadable) {
                boolean isRead = false;
                int count = 1;
                int maxCount = 3;
                while (!isRead && count <= maxCount) {
                    try {
                        Log.d(TAG, "run: trying for " + count + " time to read " + controller);
                        ((ArduinoReadable) controller).read();
                        isRead = true;
                    } catch (IOException ignored) {
                        Log.d(TAG, "couldn't read initial state of " + controller);
                        // todo retry in some time. Have to create some helper class for alarms/retries
                        // remove this temporary solution
                        ++count;
                    }
                }
            }
        }
    }

    public IotDevice getByGuid(long guid) {
        for (IotDevice device : devices) {
            if (guid == device.guid) {
                return device;
            }
        }
        throw new IllegalArgumentException("No device with guid=" + guid);
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

    @NotNull
    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }

    public void removeAll() {
        for (IotDevice device : devices) {
            GUID.getInstance().remove(device.guid);
        }
        devices.clear();
    }
}
