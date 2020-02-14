package smarthome.client

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import smarthome.client.arduino.di.arduino
import smarthome.client.data.di.data
import smarthome.client.domain.di.domain
import smarthome.client.plugingate.plugingate
import smarthome.client.presentation.di.presentation

fun startDi(context: Context) {
    startKoin {
        androidLogger()
        androidContext(context)
        modules(listOf(
            domain,
            data,
            presentation,
            arduino,
            plugingate
        ))
    }
}
