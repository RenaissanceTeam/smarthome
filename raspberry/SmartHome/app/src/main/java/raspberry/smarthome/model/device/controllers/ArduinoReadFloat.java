package raspberry.smarthome.model.device.controllers;

import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.IotDevice;

public class ArduinoReadFloat implements BaseController, Readable<Float> {
    private ArduinoIotDevice device;
    private int id;

    public ArduinoReadFloat(ArduinoIotDevice device, int id) {
        this.device = device;
        this.id = id;
    }

    @Override
    public Float read() {
        return 123.1f; // todo mqtt request here
    }

    @Override
    public IotDevice getDevice() {
        return device;
    }

    @Override
    public int getId() {
        return id;
    }
}
