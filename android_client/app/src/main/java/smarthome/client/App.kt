package smarthome.client

import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import smarthome.client.arduino.scripts.data.ArduinoTypeAdapters
import smarthome.client.data.scripts.ScriptTypeAdapters

class App : MultiDexApplication() {
    override fun onCreate() {
        Stetho.initializeWithDefaults(this)
        
        super.onCreate()
        startDi(this)
    
        configureTypeAdapters()
    }
    
    private fun configureTypeAdapters() {
        ArduinoTypeAdapters()
        ScriptTypeAdapters()
    }
}