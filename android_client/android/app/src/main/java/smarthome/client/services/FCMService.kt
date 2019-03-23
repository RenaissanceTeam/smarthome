package smarthome.client.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import smarthome.client.BuildConfig.DEBUG
import smarthome.client.util.FcmTokenStorage

class FCMService: FirebaseMessagingService() {

    private val TAG = "FCMService"
    override fun onNewToken(newToken: String?) {
        FcmTokenStorage(baseContext).savedToken = newToken ?: return
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        if (DEBUG) Log.d(TAG, "received message=$message")
    }
}