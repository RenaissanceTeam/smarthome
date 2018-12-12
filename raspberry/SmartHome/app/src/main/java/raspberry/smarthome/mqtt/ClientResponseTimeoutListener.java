package raspberry.smarthome.mqtt;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.LogPrinter;
import android.util.LongSparseArray;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import raspberry.smarthome.model.GUID;
import raspberry.smarthome.model.device.controllers.ArduinoController;


public class ClientResponseTimeoutListener implements Handler.Callback {

    public static final String KEY_TOPIC = "topic";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMEOUT = "timeout";
    public static final String KEY_GUID = "guid";
    private static ClientResponseTimeoutListener instance;
    private Context context;
    private Handler timeoutHandler;
    private LongSparseArray<Object> tokensStorage = new LongSparseArray<>();
    public static final String TAG =ClientResponseTimeoutListener.class.getSimpleName();

    private ClientResponseTimeoutListener() {}

    public static ClientResponseTimeoutListener getInstance() {
        if (instance == null) {
            instance = new ClientResponseTimeoutListener();
            return instance;
        }
        if (instance.context == null) {
            throw new IllegalStateException("can't use instance without calling init() first!");
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        timeoutHandler = new Handler(Looper.getMainLooper(), this);
    }

    public void startTimeout(long guid, String topic, String messageValue, long timeout) {
        Log.d(TAG, "startTimeout() called with: guid = [" + guid +
                "], topic = [" + topic + "], messageValue = [" + messageValue + "], timeout = ["
                + timeout + "]");
        Message message = Message.obtain();
        Bundle data = new Bundle();
        data.putString(KEY_TOPIC, topic);
        data.putString(KEY_MESSAGE, messageValue);
        data.putLong(KEY_TIMEOUT, timeout);
        data.putLong(KEY_GUID, guid);
        Object storedToken = tokensStorage.get(guid);
        if (storedToken == null) {
            storedToken = new Object();
            tokensStorage.put(guid, storedToken);
        }

        message.obj = storedToken;
        message.setData(data);

        timeoutHandler.sendMessageDelayed(message, timeout);
    }

    public void stopTimeout(long guid) {
        Log.d(TAG, "stopTimeout() called with: guid = [" + guid + "]");
//        timeoutHandler.getLooper().dump(new LogPrinter(Log.DEBUG, TAG), "before\t");
        Object storedToken = tokensStorage.get(guid);
        if (storedToken != null) {
            timeoutHandler.removeCallbacksAndMessages(storedToken);
        }

//        timeoutHandler.getLooper().dump(new LogPrinter(Log.DEBUG, TAG), "after\t");
    }

    public void stopAllTimeouts() {
        timeoutHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean handleMessage(Message msg) {
        Bundle data = msg.getData();
        String topic = data.getString(KEY_TOPIC);
        String message = data.getString(KEY_MESSAGE);
        long timeout = data.getLong(KEY_TIMEOUT);
        long guid = data.getLong(KEY_GUID);

        if (topic != null && message != null) {
            Log.d(TAG, "timeout has come for topic=" + topic + ", message=" + message);
            try {
                // todo notify android about unsuccessfull mqtt operation
                SmartHomeMqttClient.getInstance().publishMessage(message, topic);
                startTimeout(guid, topic, message, timeout);
            } catch (MqttException | UnsupportedEncodingException e) {
                Log.e(TAG, "can't send retry message");
            }
        }
        return true;
    }
}
