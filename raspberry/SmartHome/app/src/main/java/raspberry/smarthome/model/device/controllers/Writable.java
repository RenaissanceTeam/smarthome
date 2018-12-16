package raspberry.smarthome.model.device.controllers;

import java.io.IOException;

public interface Writable {
    /**
     * NOTE !! Synchronous !! http post request to device's web server
     * @param value {0, 1} for digital arduino pin, [0..1] for analog arduino pin, etc...
     * @return return body of response from device's web server
     * @throws IOException
     */
    String write(String value) throws IOException;
}
