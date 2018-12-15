package raspberry.smarthome.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.IotDevice;

import static raspberry.smarthome.MainActivity.DEBUG;

public class DevicesStorage {
    public static final String TAG = DevicesStorage.class.getSimpleName();
    private static DevicesStorage sInstance;
    public final List<IotDevice> devices = new ArrayList<>();

    private DevicesStorage() {}

    public static DevicesStorage getInstance() {
        if (sInstance == null) {
            sInstance = new DevicesStorage();
        }
        return sInstance;
    }

    public void addDevice(IotDevice device) {
        if (DEBUG) Log.d(TAG, "addDevice: " + device);
        if (devices.contains(device)) return;
        devices.add(device);
        if (DEBUG) Log.d(TAG, "addDevice: device added. Current devices=" + devices);
    }

    public void reset() {
        devices.clear();
    }

    public IotDevice getByGuid(long guid) {
        for (IotDevice device : devices) {
            if (guid == device.guid) {
                return device;
            }
        }
        throw new IllegalArgumentException("No device with guid=" + guid);
    }

    public ArduinoIotDevice getArduinoByIp(String ip) {
        for (IotDevice device : devices) {
            if (device instanceof ArduinoIotDevice) {
                if (((ArduinoIotDevice) device).ip.equals(ip)) {
                    return (ArduinoIotDevice) device;
                }
            }
        }
        throw new IllegalArgumentException("No device with ip=" + ip);
    }
}
