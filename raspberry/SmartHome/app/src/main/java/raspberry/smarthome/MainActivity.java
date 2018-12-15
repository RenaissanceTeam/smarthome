package raspberry.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.Collections;
import java.util.List;

import static raspberry.smarthome.model.device.constants.Constants.RC_SIGN_IN;


public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final boolean DEBUG = BuildConfig.DEBUG;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (DEBUG) Log.d(TAG, "onCreate");
        // todo start web server to receive notifications from Arduino
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
