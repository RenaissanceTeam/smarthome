package smarthome.raspberry

import android.app.Application

import smarthome.raspberry.model.SmartHomeRepository
import smarthome.raspberry.utils.HomeController

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        SmartHomeRepository.init(applicationContext)
    }

    companion object {
        var instance: App? = null
            private set
    }
}
