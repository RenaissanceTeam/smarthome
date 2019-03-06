package smarthome.raspberry.model.device.listeners;

import smarthome.raspberry.model.device.constants.States;

/**
 * Basic callback interface
 * <br>All listeners must implement this interface
 */
public interface BaseListener {

    /**
     * @param state
     * @see States
     */
    void onStateChanged(States state);

}
