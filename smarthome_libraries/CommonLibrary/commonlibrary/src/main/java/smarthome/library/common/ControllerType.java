package smarthome.library.common;

import static smarthome.library.common.constants.ControllerIds.*;

public enum ControllerType {
    ARDUINO_ANALOG(A_ANALOG),
    ARDUINO_ON_OFF(A_ONOFF),
    TEMPERATURE_DHT11(A_TEMPERATURE_DHT11),
    TEMPERATURE_DHT22(A_TEMPERATURE_DHT22),
    HUMIDITY_DHT11(A_HUMIDITY_DHT_11),
    HUMIDITY_DHT22(A_HUMIDITY_DHT_22),
    DIGITAL_ALERT(A_DIGITAL_ALERT),
    INIT(A_INIT),
    PRESSURE(A_PRESSURE);

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
