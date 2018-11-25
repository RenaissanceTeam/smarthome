package raspberry.smarthome.mqtt;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static raspberry.smarthome.MainActivity.DEBUG;

public class MessageListener implements IMqttMessageListener {
    public static final String TAG = MessageListener.class.getSimpleName();
    private static MessageListener sInstance;

    private MessageListener() {}

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

        // todo process messages here
        switch (topic) {
            case SmartHomeMqttClient.CLIENT_THEME: {
                setupNewClient(message);
                break;
            }
            case SmartHomeMqttClient.IOT_WELCOME_THEME: {
                setupNewIotDevice(message);
                break;
            }
            case SmartHomeMqttClient.IOT_DEVICE_THEME: {
                performAction(message);
                break;
            }
        }
    }

    private void setupNewClient(MqttMessage message) {
        // todo implement
    }


    private void setupNewIotDevice(MqttMessage message) {
        // todo implement
    }

    private void performAction(MqttMessage message) {
        // todo implement
    }



}
