package smarthome.raspberry.arduinodevices.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate


private val domain = module {
}

private val data = module {
    single<Gson>(named(arduinoApiGson)) {
        GsonBuilder()
                .setLenient()
                .create()
    }

    single<RestOperations> { RestTemplate() }
}

val arduinoModule = listOf(
        domain,
        data
)

private const val deviceGson = "deviceGson"
private const val arduinoApiGson = "arduinoApiGson"
const val arduinoDevices = "arduinoDevicesMap"
const val arduinoControllers = "arduinoControllers"
const val arduinoStates = "arduinoStates"