package raspberry.smarthome.mqtt;

import android.support.annotation.NonNull;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import raspberry.smarthome.model.DevicesStorage;
import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.IotDevice;
import raspberry.smarthome.model.device.controllers.ArduinoController;
import raspberry.smarthome.model.device.controllers.BaseController;
import raspberry.smarthome.model.device.controllers.ControllerTypes;
import raspberry.smarthome.model.device.controllers.Writable;

import static raspberry.smarthome.MainActivity.DEBUG;
import static raspberry.smarthome.mqtt.MqttThemes.*;

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

        String messageValue = getMessageValue(message);
        // todo maybe rewrite, but not now.

        // some mqtt themes are defined as generic, so we are checking the start, without wildcard
        if (topic.startsWith(takeStartFromGenericTheme(RASPBERRY_DEVICE_THEME))) {
            performAction(topic, messageValue);
            return;
        } else if (topic.startsWith(takeStartFromGenericTheme(RESULT_FROM_IP_THEME))) {
            String ip = getStringByPattern(".+\\/(.+)$", topic, 1);
            String serviceIndex = getStringByPattern("service_index=(-?\\d+);", messageValue, 1);
            ArduinoIotDevice arduinoByIp = DevicesStorage.getInstance().getArduinoByIp(ip);
            ArduinoController controller = (ArduinoController) arduinoByIp.controllers.get(Integer.parseInt(serviceIndex));
            String result = getStringByPattern("result=(.+);", messageValue, 1);

            if (DEBUG) Log.d(TAG, "result= " + result + ", for device=" +
                    arduinoByIp + ". For controller=" + controller);
            try {
                SmartHomeMqttClient.getInstance().publishMessage(
                        "controller_guid=" + controller.guid + ";result=" + result + ";", // message
                        CLIENT_RESULT_FROM_DEVICE + arduinoByIp.guid // theme
                        );
            } catch (MqttException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return;
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
                    .publishMessage("", INITIALIZE_IOT_THEME);
        } catch (MqttException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setupNewClient(MqttMessage message) {
        // todo implement
    }

    private void setupNewIotDevice(MqttMessage message) {
        String messageValue = getMessageValue(message);
        String type = getStringByPattern("type=(\\d+);", messageValue, 1);
        if (!type.equals("0")) {
            throw new RuntimeException("Mqtt init not from arduino (0) device");
        }

        String name = getName(messageValue);
        String description = getDescription(messageValue);
        String ip = getIp(messageValue);

        ArduinoIotDevice device = new ArduinoIotDevice(name, description, ip);
        initControllers(device, messageValue);

        DevicesStorage.getInstance().addDevice(device);
    }

    private void performAction(String topic, String message) {
        String guid = getStringByPattern("(\\w+\\/){2}(.+)", topic, 2);
        IotDevice iotDevice = DevicesStorage.getInstance().getByGuid(Long.parseLong(guid));

        if (!(iotDevice instanceof ArduinoIotDevice)) {
            throw new RuntimeException("Not implemented"); // todo implement for non arduino if they use mqtt
        }

        ArduinoIotDevice aIotDevice = (ArduinoIotDevice) iotDevice;

        long controllerGuid = Long.parseLong(getStringByPattern("contr_guid=(-?\\d+);", message, 1));
        ArduinoController aController = aIotDevice.getControllerByGuid(controllerGuid);

        String value = getStringByPattern("value=([\\d\\w\\s]+);", message, 1);
        if (aController instanceof Writable) {
            ((Writable) aController).write(value);
        } else if (aController instanceof Readable) {
            // todo implement readable
        }
        if (DEBUG) Log.d(TAG, "performAction: with " + iotDevice);
        if (DEBUG) Log.d(TAG, "performAction: payload= " + message);
    }

    private String getName(String message) {
        return getStringByPattern("name=(\\w+);", message, 1);
    }

    private String getDescription(String message) {
        return getStringByPattern("description=(\\w+);", message, 1);
    }

    private String getIp(String message) {
        return getStringByPattern("ip=((\\d+\\.){3}\\d+)", message, 1);
    }

    private void initControllers(ArduinoIotDevice device, String message) {
        List<Integer> services = new ArrayList<>();
        ArrayList<BaseController> controllers = new ArrayList<>();

        String servicesRaw = getStringByPattern("services=\\[(.+)\\];", message, 1);

        for (String serviceRaw : servicesRaw.split(", ")) {
            services.add(Integer.parseInt(serviceRaw));
        }

        for (int i = 0; i < services.size(); i++) {
            ControllerTypes type = ControllerTypes.getById(services.get(i));
            ArduinoController controller = type.createArduinoController(device, i);
            controllers.add(controller);
        }

        device.controllers = controllers;
    }

    @NonNull
    private String getMessageValue(MqttMessage message) {
        return new String(message.getPayload());
    }

    private String getStringByPattern(String pattern, String from, int group) {
        Matcher matcher = Pattern.compile(pattern).matcher(from);
        if (matcher.find()) {
            return matcher.group(group);
        }
        return null;
    }


}
