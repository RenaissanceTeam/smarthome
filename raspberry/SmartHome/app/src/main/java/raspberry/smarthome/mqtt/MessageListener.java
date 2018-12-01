package raspberry.smarthome.mqtt;

import android.support.annotation.NonNull;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import raspberry.smarthome.model.DevicesStorage;
import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.IotDevice;
import raspberry.smarthome.model.device.controllers.BaseController;
import raspberry.smarthome.model.device.controllers.ControllerTypes;
import raspberry.smarthome.model.device.controllers.Writable;

import static raspberry.smarthome.MainActivity.DEBUG;
import static raspberry.smarthome.mqtt.SmartHomeMqttClient.*;

public class MessageListener implements IMqttMessageListener {
    public static final String TAG = MessageListener.class.getSimpleName();
    private static MessageListener sInstance;

    private MessageListener() {
    }

    public static MessageListener getInstance() {
        if (sInstance == null) {
            sInstance = new MessageListener();
        }
        return sInstance;
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        if (DEBUG) Log.d(TAG, "messageArrived() called with: topic = [" + topic +
                "], message = [" + message + "]");

        // todo maybe rewrite, but not now.

        // some mqtt themes are defined as generic, so we are checking the start, without wildcard
        if (topic.startsWith(takeStartFromGenericTheme(IOT_DEVICE_THEME))) {
            performAction(topic, message);
        } else if (topic.startsWith(takeStartFromGenericTheme(RESULT_FROM_IP_THEME))) {
            // todo get guid by ip and publish result to "iot/device/result/{guid}"
            if (DEBUG)
                Log.d(TAG, "message " + topic + ", payload=" + new String(message.getPayload()));
        }


        switch (topic) {
            case CLIENT_THEME: {
                setupNewClient(message);
                break;
            }
            case IOT_WELCOME_THEME: {
                setupNewIotDevice(message);
                break;
            }
            case INITIALIZE_RASP_THEME: {
                publishInitializeIotRequest();
                DevicesStorage.getInstance().reset();
                break;
            }
        }
    }

    @NonNull
    private String takeStartFromGenericTheme(String theme) {
        return theme.substring(0, theme.length() - 1);
    }

    private void publishInitializeIotRequest() {
        try {
            if (DEBUG) Log.d(TAG, "publish init iot request");
            SmartHomeMqttClient.getInstance()
                    .publishMessage("", SmartHomeMqttClient.INITIALIZE_IOT_THEME);
        } catch (MqttException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setupNewClient(MqttMessage message) {
        // todo implement
    }

    private void setupNewIotDevice(MqttMessage message) {
        String name = getName(message);
        String description = getDescription(message);
        String ip = getIp(message);
        ControllerTypes[] controllers = getControllers(message);
        ArduinoIotDevice device = new ArduinoIotDevice(name, description, ip, controllers);
        DevicesStorage.getInstance().addDevice(device);
    }

    private void performAction(String topic, MqttMessage message) {
        String guid = getStringByPattern("(\\w+\\/){2}(.+)", topic, 2);
        IotDevice iotDevice = DevicesStorage.getInstance().getByGuid(Long.parseLong(guid));
        BaseController controller = iotDevice.getControllers().get(0); // todo get id of controller from message
        if (controller instanceof Writable) {
            ((Writable) controller).write(123);
        }
        if (DEBUG) Log.d(TAG, "performAction: with " + iotDevice);
        if (DEBUG) Log.d(TAG, "performAction: payload= " + new String(message.getPayload()));
    }

    private String getName(MqttMessage message) {
        return "StubName"; // todo parse message here
    }

    private String getDescription(MqttMessage message) {
        return "StubDescription"; // todo parse message here
    }

    private String getIp(MqttMessage message) {
        return getStringByPattern("ip=\"((\\d+\\.){3}\\d+)\"", new String(message.getPayload()), 1);
    }

    private String getStringByPattern(String pattern, String from, int group) {
        Matcher matcher = Pattern.compile(pattern).matcher(from);
        if (matcher.find()) {
            return matcher.group(group);
        }
        return null;
    }

    private ControllerTypes[] getControllers(MqttMessage message) {
        return new ControllerTypes[]{ControllerTypes.ARDUINO_ON_OFF}; // todo parse message here
    }
}
