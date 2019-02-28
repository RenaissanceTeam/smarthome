package ru.smarthome.model.device.listeners;

import ru.smarthome.model.device.constants.States;

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
