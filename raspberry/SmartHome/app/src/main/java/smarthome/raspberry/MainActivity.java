package smarthome.raspberry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.smarthome.auth.GoogleSignInActivity;
import ru.smarthome.server.StoppableServer;
import ru.smarthome.server.UdpServer;
import ru.smarthome.server.WebServer;

import smarthome.raspberry.auth.GoogleSignInActivity;
import smarthome.raspberry.server.StoppableServer;
import smarthome.raspberry.server.UdpServer;
import smarthome.raspberry.server.WebServer;

import static smarthome.raspberry.model.device.constants.Constants.RC_SIGN_IN;


public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final boolean DEBUG = BuildConfig.DEBUG;
    private WebServer httpServer;
    private UdpServer udpServer;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (DEBUG) Log.d(TAG, "onCreate");

        httpServer = new WebServer();
        udpServer = new UdpServer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startServer(httpServer);
        startServer(udpServer);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        auth();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopServer(httpServer);
        stopServer(udpServer);
    }

    private void auth() {
        // check for auth
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null)
            startActivity(new Intent(this, GoogleSignInActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        } else {
            // retry login
        }
    }

    private void startServer(StoppableServer server) {
        if (server == null) return;
        server.startServer();
    }

    private void stopServer(StoppableServer server) {
        if (server == null) return;
        server.stopServer();
    }
}
