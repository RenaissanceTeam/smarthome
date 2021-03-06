package smarthome.client.presentation.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.domain.api.notifications.SaveNotificationTokenUseCase
import smarthome.client.presentation.R

class FCMService : FirebaseMessagingService(), KoinComponent {
    private var notificationManager: NotificationManager? = null
    private val saveNotificationTokenUseCase: SaveNotificationTokenUseCase by inject()
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()

        notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = getString(R.string.alert_notification_channel)

        createNotificationChannel(channel, channel, channel)
    }

    override fun onDestroy() {
        super.onDestroy()

        job.cancel()
    }

    override fun onNewToken(newToken: String) {
        ioScope.launch { saveNotificationTokenUseCase.execute(newToken) }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val notification = message.notification ?: return

        val notificationBuilder =
                NotificationCompat.Builder(this, getString(R.string.alert_notification_channel))
                        .setSmallIcon(R.drawable.round_warning)
                        .setContentTitle(notification.title)
                        .setContentText(notification.body)


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