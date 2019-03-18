package smarthome.client.services

import com.google.firebase.messaging.FirebaseMessagingService
import smarthome.client.util.FcmTokenStorage

class FCMService: FirebaseMessagingService() {

    override fun onNewToken(newToken: String?) {
        FcmTokenStorage(baseContext).savedToken = newToken ?: return
    }
}