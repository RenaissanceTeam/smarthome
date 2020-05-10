package smarthome.client

import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import org.koin.android.ext.android.get
import smarthome.client.arduino.scripts.data.ArduinoTypeAdapters
import smarthome.client.data.api.notifications.NotificationRepository
import smarthome.client.data.scripts.ScriptTypeAdapters

class App : MultiDexApplication() {
    override fun onCreate() {
        Stetho.initializeWithDefaults(this)

        super.onCreate()
        startDi(this)

        configureTypeAdapters()
        createInstancesOnStartOfApp()
    }

    private fun configureTypeAdapters() {
        ArduinoTypeAdapters()
        ScriptTypeAdapters()
    }

    private fun createInstancesOnStartOfApp() {
        get<NotificationRepository>()
    }
}