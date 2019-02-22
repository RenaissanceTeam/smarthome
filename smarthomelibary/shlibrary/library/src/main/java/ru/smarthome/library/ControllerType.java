package ru.smarthome.library;

import static ru.smarthome.library.constants.ControllerIds.*;

public enum ControllerType {
    ARDUINO_ANALOG(A_ANALOG),
    ARDUINO_ON_OFF(A_ONOFF),
    TEMPERATURE(A_TEMPERATURE),
    HUMIDITY(A_HUMIDITY),
    DIGITAL_ALERT(A_DIGITAL_ALERT),
    INIT(A_INIT);

    public final int id;

    ControllerType(int id) {
        this.id = id;
    }

    public static ControllerType getById(int id) {
        for (ControllerType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("No such controller type=" + id);
    }
}
