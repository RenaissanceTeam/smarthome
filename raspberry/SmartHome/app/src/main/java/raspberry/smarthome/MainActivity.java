package raspberry.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import io.moquette.BrokerConstants;
import io.moquette.server.Server;
import io.moquette.server.config.MemoryConfig;
import raspberry.smarthome.auth.GoogleSignInActivity;
import raspberry.smarthome.model.device.constants.Constants;
import raspberry.smarthome.mqtt.SmartHomeMqttClient;

import static raspberry.smarthome.model.device.constants.Constants.RC_SIGN_IN;


public class MainActivity extends Activity implements SmartHomeMqttClient.OnConnectionChange{

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final boolean DEBUG = BuildConfig.DEBUG;
    private Server mqttServer;
    private SmartHomeMqttClient smartHomeMqttClient;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (DEBUG) Log.d(TAG, "onCreate");

        if (!tryStartServer()) return;
        setupLocalMqttClient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        auth();
    }

    private void auth() {
        // check for auth
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null)
            startActivity(new Intent(this, GoogleSignInActivity.class));
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
            smartHomeMqttClient = SmartHomeMqttClient.getInstance();
            smartHomeMqttClient.init(this, Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);
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
        // adb shell rm -rf /sdcard/moquette_store.mapdb
        new File(path).delete();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            } else {
                // TODO: guide user to the xuy
            }
        }
    }
}
