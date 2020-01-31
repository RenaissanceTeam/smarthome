package smarthome.client

import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import smarthome.client.data.di.data
import smarthome.client.domain.di.domain
import smarthome.client.presentation.di.presentation

class App : MultiDexApplication() {
    override fun onCreate() {
        Stetho.initializeWithDefaults(this)
        
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(
                domain,
                data,
                presentation
            ))
        }
    }
}