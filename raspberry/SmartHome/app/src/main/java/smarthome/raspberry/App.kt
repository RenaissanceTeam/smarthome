package smarthome.raspberry

import android.app.Application
import android.content.Context
import android.os.PowerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.raspberry.domain.HomeRepository
import smarthome.raspberry.domain.usecases.HomeUseCase

class App : Application() {
    var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate() {
        super.onCreate()

        wakeLock =
                (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                    newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SmartHome::WakeLock").apply {
                        acquire()
                    }
                }
    }

    override fun onTerminate() {
        super.onTerminate()
        wakeLock?.release()
//        val storage = FirestoreSmartHomeStorage.getInstance(
//                SharedPreferencesHelper.getInstance(this).getHomeId()
//        )
//        storage?.detachDevicesUpdatesObserver()
//        storage?.detachPendingDevicesUpdatesObserver()
    }
}
