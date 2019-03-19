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

        long shift = 1;
        while (mGuids.contains(guid)) {
            guid += shift;
        }

        // so we've created a unique guid for the controller
        mGuids.add(guid);
        return guid;
    }

    public long issueNewControllerGuid(BaseController controller) {
        long deviceHash = controller.deviceType.hashCode();
        long guid = deviceHash;

        Random r = new Random();

        while (!mGuids.add(guid))
            guid = deviceHash + Math.abs(r.nextInt(99999));

        return guid;
    }

    private long getGuidForName(String name) {
        return name.hashCode();
    }

}
