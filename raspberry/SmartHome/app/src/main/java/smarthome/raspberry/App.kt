package smarthome.raspberry

import android.app.Application
import android.content.Context
import android.os.PowerManager
import smarthome.library.datalibrary.store.firestore.FirestoreSmartHomeStorage
import smarthome.raspberry.utils.SharedPreferencesHelper

class App : Application() {

    var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate() {
        super.onCreate()
        instance = this

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
        FirestoreSmartHomeStorage.getInstance(
                SharedPreferencesHelper.getInstance(this).getHomeId()
        )?.detachDevicesUpdatesObserver()
    }

    companion object {
        var instance: App? = null
            private set
    }
}
