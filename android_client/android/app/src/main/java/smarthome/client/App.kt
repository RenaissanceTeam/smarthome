package smarthome.client

import android.content.Context
import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.client.util.FcmTokenRequester

class App : MultiDexApplication() {
    private val TAG = "App"

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}