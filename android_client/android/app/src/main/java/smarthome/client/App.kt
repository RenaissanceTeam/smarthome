package smarthome.client

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.client.BuildConfig.DEBUG
import smarthome.client.util.FcmTokenRequester
import smarthome.client.util.FcmTokenStorage
import java.lang.RuntimeException
import kotlin.coroutines.suspendCoroutine

class App : MultiDexApplication() {
    private val TAG = "App"

    private val ioScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        ioScope.launch {
            FcmTokenRequester(applicationContext).initFcmToken()
        }
    }


}