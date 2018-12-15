package raspberry.smarthome.mqtt;

import android.util.Log;

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

public class MessageListener {
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

    private void handleResultFromIot(String topic, String messageValue) {
        String ip = getStringByPattern(".+\\/(.+)$", topic, 1);
        String serviceIndex = getStringByPattern("service_index=(-?\\d+);", messageValue, 1);
        ArduinoIotDevice arduinoByIp = DevicesStorage.getInstance().getArduinoByIp(ip);
        ArduinoController controller = (ArduinoController) arduinoByIp.controllers.get(Integer.parseInt(serviceIndex));
        String result = getStringByPattern("result=(.+);", messageValue, 1);

        if (DEBUG) Log.d(TAG, "result= " + result + ", for device=" +
                arduinoByIp + ". For controller=" + controller);
        // todo send response to android client
    }


    private void setupNewClient() {
        // todo implement
    }

    private void setupNewIotDevice() {
        String messageValue = ""; // todo message value from http body response
        String type = getStringByPattern("type=(\\d+);", messageValue, 1);


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

    private String getStringByPattern(String pattern, String from, int group) {
        Matcher matcher = Pattern.compile(pattern).matcher(from);
        if (matcher.find()) {
            return matcher.group(group);
        }
        return null;
    }


}
