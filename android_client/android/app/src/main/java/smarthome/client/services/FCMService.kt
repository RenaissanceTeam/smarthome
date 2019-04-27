package smarthome.client.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.NotificationManager.IMPORTANCE_LOW
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import smarthome.client.BuildConfig.DEBUG
import smarthome.client.R
import smarthome.client.util.FcmTokenStorage

const val DATA_MESSAGE_TITLE_KEY = "title"
const val DATA_MESSAGE_BODY_KEY = "body"

class FCMService : FirebaseMessagingService() {

    private val TAG = "FCMService"

    private var notificationManager: NotificationManager? = null

    override fun onCreate() {
        super.onCreate()

        notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannels()
    }

    override fun onNewToken(newToken: String?) {
        FcmTokenStorage(baseContext).savedToken = newToken ?: return
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        if (DEBUG) Log.d(TAG, "received message=$message")

        message ?: return

        val notification = message.notification
        notification?.let {
            processNotificationMessage(it)
            return
        }

        processDataMessage(message.data)
    }

    private fun processNotificationMessage(notification: RemoteMessage.Notification) {
        val messageTitle = notification.title ?: "Error"
        val messageBody = notification.body ?: ""

        val notificationBuilder = NotificationCompat.Builder(this, getString(R.string.alert_notification_channel))
                .setSmallIcon(R.drawable.round_warning)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)

        showNotification(notificationBuilder.build())
    }

    private fun processDataMessage(dataMessage: MutableMap<String, String>) {
        val notificationBuilder = NotificationCompat.Builder(this, getString(R.string.silent_alert_notification_channel))
                .setSmallIcon(R.drawable.round_warning)
                .setContentTitle(dataMessage[DATA_MESSAGE_TITLE_KEY])
                .setContentText(dataMessage[DATA_MESSAGE_BODY_KEY])
                .setPriority(IMPORTANCE_LOW)
                .setVibrate(null)

        showNotification(notificationBuilder.build())
    }

    private fun showNotification(notification: Notification) { //TODO: add actions
        val notificationId = System.currentTimeMillis().toInt()

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, notification)
        }
    }

    private fun createNotificationChannels() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return

        val alertChannelName = getString(R.string.alert_notification_channel)

        val alertChannel = NotificationChannel(alertChannelName, alertChannelName, IMPORTANCE_HIGH)
        alertChannel.description = alertChannelName
        notificationManager?.createNotificationChannel(alertChannel)

        val alertSilentChannelName = getString(R.string.silent_alert_notification_channel)

        val alertSilentChannel = NotificationChannel(alertSilentChannelName, alertSilentChannelName, IMPORTANCE_LOW)
        alertSilentChannel.description = alertSilentChannelName
        alertSilentChannel.enableVibration(false)
        notificationManager?.createNotificationChannel(alertSilentChannel)
    }
}