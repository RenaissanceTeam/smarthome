package smarthome.library.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import kotlin.random.Random;

/**
 * IoT device can be identified by its guid. As well as its controllers
 */
public class GUID {
    private static GUID instance;
    private final Set<Long> mGuids = new HashSet<>();

    private GUID() {}

    public static GUID getInstance() {
        if (instance == null) {
            instance = new GUID();
        }
        return instance;
    }

    public void remove(long guid) {
        mGuids.remove(guid);
    }

    public long getGuidForIotDevice(IotDevice device) {
        return getGuidForName(device.getName());
    }

    public long generateGuidForController(IotDevice device, BaseController controller) {
        long guid = getGuidForName(controller.getName());
        mGuids.add(guid);
        return guid;
    }

    public String getGuidForMessage() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss");
        return sdf.format(new Date());
    }

    private long getGuidForName(String name) {
        return name.hashCode();
    }

    public long random() {
        return Random.Default.nextLong();
    }
}
