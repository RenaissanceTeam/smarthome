package smarthome.raspberry;

import android.app.Application;
import android.util.Log;

import static smarthome.raspberry.BuildConfig.DEBUG;


public class App extends Application {

    public static final String APP_TAG = App.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();

        if (DEBUG) Log.d(APP_TAG, "App created");
    }
}
