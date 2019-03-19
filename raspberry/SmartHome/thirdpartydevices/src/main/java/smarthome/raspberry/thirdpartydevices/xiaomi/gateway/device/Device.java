package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device;

import smarthome.library.common.IotDevice;

public abstract class Device extends IotDevice {

    static final String IP_KEY = "ip";
    static final String RGB_KEY = "rgb";
    static final String ILLUMINATION_KEY = "illumination";
    static final String PROTO_VERSION_KEY = "proto_version";

    public static final String STATUS_KEY = "status";
    static final String STATUS_OPEN = "open";
    static final String STATUS_CLOSE = "close";
    static final String STATUS_MOTION = "motion";
    static final String STATUS_NO_MOTION = "no_motion";
    static final String STATUS_CLICK = "click";
    static final String STATUS_DOUBLE_CLICK = "double_click";
    static final String STATUS_LONG_PRESS = "long_click_press";
    static final String STATUS_TEMPERATURE = "temperature";
    static final String STATUS_HUMIDITY = "humidity";
    static final String STATUS_WATER_LEAK = "leak";
    static final String STATUS_NO_WATER_LEAK = "no_leak";
    static final String STATUS_PRESSURE = "pressure";
    public static final String STATUS_CHANNEL_0 = "channel_0";
    public static final String STATUS_CHANNEL_1 = "channel_1";

    static final String STATUS_ON = "on";
    static final String STATUS_OFF = "off";

    static final String VOLTAGE_KEY = "voltage";
    static final String IN_USE_KEY = "inuse";
    static final String POWER_CONSUMED_KEY = "power_consumed";
    static final String LOAD_POWER_KEY = "load_power";
    static final String ALARM_KEY = "alarm";
    static final String DENSITY_KEY = "density";

    private String sid;

    public Device(String sid, String type) {
        this.sid = sid;
        setType(type);
    }

    public String getSid() {
        return sid;
    }

    public abstract void parseData(String cmd);

    @Override
    public String toString() {
        return "|\n--- Xiaomi gateway device ---\n" + "type: " + getType() + ", sid: " + sid + ", name: " + name;
    }
}
