package smarthome.raspberry

import android.app.Application
import android.content.Context
import android.os.PowerManager
import smarthome.raspberry.domain.HomeRepository
import smarthome.raspberry.domain.usecases.DevicesUseCase
import smarthome.raspberry.domain.usecases.HomeUseCase

class App : Application() {
    var wakeLock: PowerManager.WakeLock? = null
    private val devicesUseCase: DevicesUseCase = TODO()
    private val homeUseCase: HomeUseCase = TODO()
    private val repository: HomeRepository = TODO()


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
    }
}
