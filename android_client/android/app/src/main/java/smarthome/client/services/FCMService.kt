package smarthome.client.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import smarthome.client.BuildConfig.DEBUG
import smarthome.client.R
import smarthome.client.util.FcmTokenStorage


class FCMService : FirebaseMessagingService() {

    private val TAG = "FCMService"

    private var notificationManager: NotificationManager? = null

    override fun onCreate() {
        super.onCreate()

        notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = getString(R.string.alert_notification_channel)

        createNotificationChannel(channel, channel, channel)
    }

    override fun onNewToken(newToken: String?) {
        FcmTokenStorage(baseContext).savedToken = newToken ?: return
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        if (DEBUG) Log.d(TAG, "received message=$message")

        message ?: return

        val notification = message.notification ?: return

        val messageTitle = notification.title
        val messageBody = notification.body

        val notificationBuilder = NotificationCompat.Builder(this, getString(R.string.alert_notification_channel))
                .setSmallIcon(R.drawable.round_warning)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)


        val notificationId = System.currentTimeMillis().toInt()

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, notificationBuilder.build())
        }
    }

    private fun createNotificationChannel(id: String, name: String,
                                          description: String) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return

        val importance = IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance)
        channel.description = description
        notificationManager?.createNotificationChannel(channel)
    }
}