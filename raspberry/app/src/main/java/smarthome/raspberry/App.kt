package smarthome.raspberry

import android.app.Application
import android.content.Context
import android.os.PowerManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import smarthome.raspberry.di.dataModule
import smarthome.raspberry.di.useCasesModule

class App : Application() {
    var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(dataModule, useCasesModule))
        }
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
    }
}
