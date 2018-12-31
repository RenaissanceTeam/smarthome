package ru.smarthome.library;

public enum ControllerType {
    ARDUINO_ANALOG(1000), ARDUINO_ON_OFF(1001), TEMPERATURE(1002), HUMIDITY(1003);

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
