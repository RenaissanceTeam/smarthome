package ru.smarthome.model.device.constants;

public enum States {
    /**
     * state indicates previously known device connected to raspberry again
     */
    CONNECTED,

    /**
     * state indicates that previously known device was disconnected
     */
    DISCONNECTED,

    /**
     * connection failed state
     */
    CONNECTION_FAILED,

    /**
     * state indicates that connection with active device has been lost
     */
    CONNECTION_LOST,

    /**
     * state indicates device is active
     */
    DEVICE_ON,

    /**
     * state indicates device is not active
     */
    DEVICE_OFF
}
