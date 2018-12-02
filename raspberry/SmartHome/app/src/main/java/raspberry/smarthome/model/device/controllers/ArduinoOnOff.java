package raspberry.smarthome.model.device.controllers;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;

import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.mqtt.SmartHomeMqttClient;

public class ArduinoOnOff extends ArduinoController implements Writable {

    public ArduinoOnOff(ArduinoIotDevice device, ControllerTypes type,
                        int indexInArduinoServicesArray) {
        super(device, type, indexInArduinoServicesArray);
    }

    @Override
    public void write(String value) {
        try {
            // TODO: 11/30/2018 message into strings
            SmartHomeMqttClient.getInstance()
                    .publishMessage(
                            "service=" + indexInArduinoServicesArray + ";" + value, // message
                            "iot/device/ip/" + device.ip); // theme
        } catch (MqttException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
