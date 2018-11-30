package raspberry.smarthome.model.device.controllers;

import raspberry.smarthome.model.device.IotDevice;

/**
 * Basic controller interface
 * <br>All controllers must implement this interface
 */
public interface BaseController {
    IotDevice getDevice();
    int getId();
}
