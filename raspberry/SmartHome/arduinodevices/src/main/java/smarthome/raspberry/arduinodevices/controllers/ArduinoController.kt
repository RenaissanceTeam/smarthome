package smarthome.raspberry.arduinodevices.controllers

import android.util.Log

import com.google.firebase.firestore.Exclude
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose

import java.io.IOException

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerType
import smarthome.library.common.GUID
import smarthome.raspberry.arduinodevices.ArduinoControllerResponse
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.ArduinoDeviceAPI

open class ArduinoController(@field:Exclude var device: ArduinoDevice,
                             name: String,
                             type: ControllerType,
                             @field:Exclude @field:Expose val indexInArduinoServicesArray: Int)
    : BaseController(name) {

    internal val arduinoDeviceAPI: ArduinoDeviceAPI
        @Exclude get() {
            val gson = GsonBuilder()
                    .setLenient()
                    .create()

            val baseUrl = "http://" + this.device.ip + ":8080/"
            val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

            return retrofit.create(ArduinoDeviceAPI::class.java!!)
        }


    @Synchronized
    fun setNewState(newState: String) {
        state = newState
        Log.d(this.javaClass.getSimpleName(), "new state: $newState")
        // todo notify all android clients about new state (if it's changed)
    }

    fun baseRead(): ArduinoControllerResponse? {
        val call = arduinoDeviceAPI.controllerReadRequest(indexInArduinoServicesArray)

        val controllerResponse = call.execute().body() // synchronous
        if (controllerResponse != null) setNewState(controllerResponse.response)
        return controllerResponse
    }

    fun baseWrite(value: String): ArduinoControllerResponse? {
        val call = arduinoDeviceAPI.controllerWriteRequest(indexInArduinoServicesArray, value)

        val controllerResponse = call.execute().body() // synchronous
        if (controllerResponse != null) setNewState(controllerResponse.response)
        return controllerResponse
    }

    override fun toString(): String {
        return "ArduinoController{" +
                "device guid=" + device.guid +
                ", name=" + name +
                ", guid=" + guid +
                ", indexInArduinoServicesArray=" + indexInArduinoServicesArray +
                ", type=" + type +
                '}'.toString()
    }
}
