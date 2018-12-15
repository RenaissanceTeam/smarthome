package raspberry.smarthome.model;

import java.util.HashSet;
import java.util.Set;

import raspberry.smarthome.model.device.IotDevice;
import raspberry.smarthome.model.device.controllers.ArduinoController;
import raspberry.smarthome.model.device.controllers.BaseController;

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

    public long getGuidForIotDevice(IotDevice device) {
        return getGuidForName(device.name);
    }

    public long getGuidForController(BaseController controller) {
        if (!(controller instanceof ArduinoController)) {
            throw new RuntimeException("Not implemented"); // todo implement for non arduino controller
        }

        ArduinoController aController = (ArduinoController) controller;

        long guid = getGuidForIotDevice(aController.device)
                * aController.type.id
                * (aController.indexInArduinoServicesArray + 1);
        long shift = 1;
        while (mGuids.contains(guid)) {
            guid += shift;
        }
        // so we've created a unique guid for the controller
        return guid;
    }


    private long getGuidForName(String name) {
        return name.hashCode();
    }
}
