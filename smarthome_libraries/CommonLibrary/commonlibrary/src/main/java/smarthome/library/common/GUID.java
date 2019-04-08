package smarthome.library.common;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
        return getGuidForName(device.name);
    }

    public long generateGuidForController(IotDevice device, BaseController controller) {
        long guid = device.guid + controller.type.hashCode();
        mGuids.add(guid);
        return guid;
    }

    private long getGuidForName(String name) {
        return name.hashCode();
    }

}
