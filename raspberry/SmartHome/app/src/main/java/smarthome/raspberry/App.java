package smarthome.raspberry;

import android.app.Application;

import smarthome.raspberry.model.SmartHomeRepository;

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        SmartHomeRepository.getInstance().init(getApplicationContext());

    }
}
