package smarthome.client

import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import smarthome.client.util.usecasesModule

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@App)
            modules(listOf(usecasesModule))
        }
    }
}