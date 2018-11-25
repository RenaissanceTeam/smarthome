package raspberry.smarthome;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import io.moquette.BrokerConstants;
import io.moquette.server.Server;
import io.moquette.server.config.MemoryConfig;
import raspberry.smarthome.model.device.constants.Constants;
import raspberry.smarthome.mqtt.SmartHomeMqttClient;

public class MainActivity extends Activity implements SmartHomeMqttClient.OnConnectionChange{

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final boolean DEBUG = BuildConfig.DEBUG;
    private Server mqttServer;
    private SmartHomeMqttClient smartHomeMqttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (DEBUG) Log.d(TAG, "onCreate");

        if (!tryStartServer()) return;
        setupLocalMqttClient();
    }

    private boolean tryStartServer() {
        try {
            mqttServer = startMqttServer();
            return true;
        } catch (IOException e) {
            if (DEBUG) Log.d(TAG, "can't start mqtt server " + e);
            return false;
        }
    }

    private void setupLocalMqttClient() {
        try {
            smartHomeMqttClient = new SmartHomeMqttClient(this,
                    Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);

            smartHomeMqttClient.connect(this);
        } catch (Exception e) {
            if (DEBUG) Log.d(TAG, "can't create mqtt client " + e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smartHomeMqttClient.disconnect();
        smartHomeMqttClient = null;

        mqttServer.stopServer();
        mqttServer = null;
    }

    private Server startMqttServer() throws IOException {
        MemoryConfig memoryConfig = new MemoryConfig(new Properties());
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + BrokerConstants.DEFAULT_MOQUETTE_STORE_MAP_DB_FILENAME;
        memoryConfig.setProperty(BrokerConstants.PERSISTENT_STORE_PROPERTY_NAME, path);
        Server server = new Server();

        // todo if crashes like java.lang.NoSuchMethodException: <init>
        // temp solution: remove moquette_store.mapdb in /sdcard
        server.startServer(memoryConfig);
        if (DEBUG) Log.d(TAG, "server started");
        return server;
    }

    @Override
    public void onConnected() {
        smartHomeMqttClient.subscribe();
    }

    @Override
    public void onFail() {
        if (DEBUG) Log.d(TAG, "on client connection Fail");
    }
}
