package smarthome.client.presentation.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.BuildConfig.DEBUG
import smarthome.client.R
import smarthome.client.domain.usecases.CloudMessageUseCase

class FCMService : FirebaseMessagingService(), KoinComponent {
    private var notificationManager: NotificationManager? = null
    private val messageUseCase: CloudMessageUseCase by inject()

    override fun onCreate() {
        super.onCreate()

        notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = getString(R.string.alert_notification_channel)

        createNotificationChannel(channel, channel, channel)
    }

    override fun onNewToken(newToken: String?) {
        messageUseCase.onNewToken(newToken)
    }

    override fun onMessageReceived(message: RemoteMessage?) {
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