package raspberry.smarthome.model.device.controllers;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;

import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.IotDevice;
import raspberry.smarthome.mqtt.SmartHomeMqttClient;

public class ArduinoOnOff implements BaseController, Writable<Integer>{
    private ArduinoIotDevice device;
    private int id;

    public ArduinoOnOff(ArduinoIotDevice device, int id) {
        this.device = device;
        this.id = id;
    }

    @Override
    public void write(Integer value) {
        try {
            // TODO: 11/30/2018 message into strings
            SmartHomeMqttClient.getInstance().publishMessage("id=" + id + ";" + value,
                    "iot/device/ip/" + device.ip);
        } catch (MqttException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
