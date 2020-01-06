package smarthome.raspberry

import org.koin.core.context.startKoin
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import smarthome.raspberry.di.appModules

@SpringBootApplication
open class RaspberryApplication

fun main(args: Array<String>) {
    CreateClassesOnAppStart()
    startKoin {
        modules(appModules)
    }
    runApplication<RaspberryApplication>(*args)
  
}

//class App : Application() {
//    var wakeLock: PowerManager.WakeLock? = null
//
//    override fun onCreate() {
//        super.onCreate()
//
//
//
//        CreateClassesOnAppStart()
//
//        wakeLock =
//                (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
//                    newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SmartHome::WakeLock").apply {
//                        acquire()
//                    }
//                }
//    }
//
//    override fun onTerminate() {
//        super.onTerminate()
//        wakeLock?.release()
//    }
//}
