package raspberry.smarthome.model.device.controllers;

import java.io.IOException;

import raspberry.smarthome.model.device.requests.ControllerResponse;

public interface Readable {
    /**
     * Synchronous http request, returns json {@link raspberry.smarthome.model.device.requests.ControllerResponse}
     */
    ControllerResponse read() throws IOException;
}
