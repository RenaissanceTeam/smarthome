package smarthome.client

import android.content.Context
import androidx.multidex.MultiDexApplication

class App : MultiDexApplication() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}