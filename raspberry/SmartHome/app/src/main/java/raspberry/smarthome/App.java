package raspberry.smarthome;

import android.app.Application;
import android.util.Log;

import raspberry.smarthome.tasks.IpObtainTask;

import static raspberry.smarthome.BuildConfig.DEBUG;

public class App extends Application {

    // execute smth once per app startServer
    public App() {
        if(DEBUG) Log.d(getClass().getName(), "App started");

        new IpObtainTask().execute();
    }
}
