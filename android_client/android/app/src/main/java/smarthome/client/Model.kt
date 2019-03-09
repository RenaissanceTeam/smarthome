package smarthome.client

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smarthome.client.constants.Constants
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.SmartHome
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.library.datalibrary.store.listeners.SmartHomeListener

// todo should not be a singleton
object Model {
    private val TAG = Model::class.java.simpleName
    private var homeStorage: SmartHomeStorage = MockHomeStorage()

    val raspberryApi: RaspberryApi
        get() {
            val gson = GsonBuilder().setLenient().create()

            val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.RASPBERRY_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

            return retrofit.create(RaspberryApi::class.java)
        }

    fun getDevices(listener: (MutableList<IotDevice>) -> Unit) {
        homeStorage.getSmartHome(SmartHomeListener { listener(it.devices) })
    }
}