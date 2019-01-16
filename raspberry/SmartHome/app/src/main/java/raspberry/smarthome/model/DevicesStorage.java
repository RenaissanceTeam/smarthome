package raspberry.smarthome.model;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.IotDevice;
import raspberry.smarthome.model.device.controllers.BaseController;
import raspberry.smarthome.model.device.controllers.Readable;

import static raspberry.smarthome.MainActivity.DEBUG;

public class DevicesStorage {
    public static final String TAG = DevicesStorage.class.getSimpleName();
    private static DevicesStorage sInstance;
    @Expose
    public final List<IotDevice> devices = new ArrayList<>();

    private DevicesStorage() {
    }

    public static DevicesStorage getInstance() {
        if (sInstance == null) {
            sInstance = new DevicesStorage();
        }
        return sInstance;
    }

    public boolean addDevice(final IotDevice device) {
        if (DEBUG) Log.d(TAG, "addDevice: " + device);
        if (devices.contains(device)) return false;
        // todo decompose into pubsub, notify every sub about new device added
        Log.d(TAG, "run: start initial controllers reading");
        new Thread() {
            @Override
            public void run() {
                for (BaseController controller : device.controllers) {
                    if (controller instanceof Readable) {
                        boolean isRead = false;
                        int count = 1;
                        int maxCount = 3;
                        while (!isRead && count <= maxCount) {
                            try {
                                Log.d(TAG, "run: trying for " + count + " time to read " + controller);
                                ((Readable) controller).read();
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
        }.start();

        // todo set alarms for auto refresh for each controller

        return devices.add(device);
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

    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }

    public void removeAll() {
        for (IotDevice device : devices) {
            device.remove();
        }
        devices.clear();
    }
}