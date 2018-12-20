package raspberry.smarthome.model.device.controllers;

/**
 * Basic controller interface
 * <br>All controllers must implement this interface
 */
public interface BaseController {
    void setNewState(String newState); // todo is "String" enough for state?
}
