package raspberry.smarthome.mqtt;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static raspberry.smarthome.MainActivity.DEBUG;
import static raspberry.smarthome.mqtt.MqttThemes.*;

public class SmartHomeMqttClient {
    public interface OnConnectionChange {
        void onConnected();
        void onFail();
    }
    private static final String TAG = SmartHomeMqttClient.class.getSimpleName();
    public static final int QOS_AT_MOST_ONCE = 0;
    public static final int QOS_AT_LEAST_ONCE = 1;
    public static final int QOS_EXACTLY_ONCE = 2;
    private MqttAndroidClient mqttClient;
    private static final String[] THEMES = new String[]{CLIENT_THEME, IOT_WELCOME_THEME,
            RASPBERRY_DEVICE_THEME, INITIALIZE_RASP_THEME, RESULT_FROM_IP_THEME};
    private static SmartHomeMqttClient instance;

    public static SmartHomeMqttClient getInstance() {
        if (instance == null) {
            instance = new SmartHomeMqttClient();
        }
        return instance;
    }

    private SmartHomeMqttClient() {}

    public void init(Context context, String brokerUrl, String clientId) {
        mqttClient = new MqttAndroidClient(context, brokerUrl, clientId);
    }

    public void connect(final OnConnectionChange listener) throws MqttException {

        mqttClient.connect(getMqttConnectionOption())
                .setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                mqttClient.setBufferOpts(getDisconnectedBufferOptions());
                listener.onConnected();
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                if (DEBUG) Log.d(TAG, "Failure " + exception.toString());
                listener.onFail();
            }
        });
    }

    public void subscribe() {
        subscribe(THEMES);
    }

    public void subscribe(String... themes) {
        try {
            for (String theme : themes) {
                mqttClient.subscribe(theme, QOS_AT_LEAST_ONCE, MessageListener.getInstance());
            }
        } catch (Exception e) {
            if (DEBUG) Log.d(TAG, "can't subscribe to themes " + Arrays.toString(themes)+ "." + e);
        }
    }

    public void disconnect() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            if (DEBUG) Log.d(TAG, "disconnect:  " + e);
        }
    }

    private DisconnectedBufferOptions getDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(false);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }

    private MqttConnectOptions getMqttConnectionOption() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
//        mqttConnectOptions.setWill(Constants.PUBLISH_TOPIC, "I am going offline".getBytes(), 1, true);
        //mqttConnectOptions.setUserName("ngbllzzy");
        //mqttConnectOptions.setPassword("WtjhZKl3OPoK".toCharArray());
        return mqttConnectOptions;
    }


    public void publishMessage(String message, String topic) throws MqttException,
            UnsupportedEncodingException {
        publishMessage(message, topic, false, 0);
    }

    public void publishMessage(String message, String topic, boolean shouldDoTimeoutRetry, long timeoutRetry)
            throws MqttException, UnsupportedEncodingException {
        byte[] encodedPayload = new byte[0];
        encodedPayload = message.getBytes("UTF-8");

        MqttMessage mqttMessage = new MqttMessage(encodedPayload);
        mqttMessage.setId(message.hashCode());
        mqttMessage.setRetained(true);
        mqttMessage.setQos(QOS_AT_LEAST_ONCE);

        mqttClient.publish(topic, mqttMessage);
    }

    public void unsubscribe(final String topic) throws MqttException {
        IMqttToken token = mqttClient.unsubscribe(topic);

        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                if (DEBUG) Log.d(TAG, "UnSubscribe Successfully " + topic);
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                if (DEBUG) Log.e(TAG, "UnSubscribe Failed " + topic);
            }
        });
    }
}