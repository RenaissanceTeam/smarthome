package raspberry.smarthome.model.device.controllers;

import android.text.format.DateUtils;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;

import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.mqtt.ClientResponseTimeoutListener;
import raspberry.smarthome.mqtt.SmartHomeMqttClient;

public class ArduinoOnOff extends ArduinoController implements Writable {

    public static final int TIMEOUT_RETRY = 1000;

    public ArduinoOnOff(ArduinoIotDevice device, ControllerTypes type,
                        int indexInArduinoServicesArray) {
        super(device, type, indexInArduinoServicesArray);
    }

    @Override
    public void write(String value) {
        try {
            // TODO: 11/30/2018 message into strings
            String message = indexInArduinoServicesArray + ";" + value;
            SmartHomeMqttClient.getInstance()
                    .publishMessage(message, getTopic());
            ClientResponseTimeoutListener.getInstance().startTimeout(guid, getTopic(), message, TIMEOUT_RETRY);
        } catch (MqttException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
