package raspberry.smarthome.model.device.controllers;

/**
 * Basic controller interface
 * <br>All controllers must implement this interface
 */
public interface BaseController {

    /**
     * turn on device method
     */
    void turnOn();

    /**
     * turn off device method
     */
    void turnOff();

    /**
     * establish connection with device method
     */
    void connect();

    /**
     * disconnect device without forgetting it
     */
    void disconnect();

    /**
     * forget device method
     */
    void forget();


}
