package smarthome.raspberry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class RaspberryApplication

fun main(args: Array<String>) {
    runApplication<RaspberryApplication>(*args)
}

//class App : Application() {
//    var wakeLock: PowerManager.WakeLock? = null
//
//    override fun onCreate() {
//        super.onCreate()
//
//        startKoin {
//            androidContext(this@App)
//            modules(appModules)
//        }
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
