package smarthome.raspberry.arduinodevices.controllers

import smarthome.library.common.BaseController
import smarthome.raspberry.arduinodevices.ArduinoDevice

open class ArduinoController(name: String,
                             val device: ArduinoDevice,
                             val indexInArduinoServicesArray: Int)
    : BaseController(name) {

//    val arduinoDeviceAPI: ArduinoDeviceAPI
//        @Exclude get() {
//            val gson = GsonBuilder()
//                    .setLenient()
//                    .create()
//
//            val baseUrl = "http://" + this.device.ip + ":8080/"
//            val retrofit = Retrofit.Builder()
//                    .baseUrl(baseUrl)
//                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .build()
//
//            return retrofit.create(ArduinoDeviceAPI::class.java)
//        }


//    fun baseRead(): ArduinoControllerResponse? {
//        val call = arduinoDeviceAPI.controllerReadRequest(indexInArduinoServicesArray)
//
//        val controllerResponse = call.execute().body() // synchronous
//        if (controllerResponse != null) setNewState(controllerResponse.response)
//        return controllerResponse
//    }
//
//    fun baseWrite(value: String): ArduinoControllerResponse? {
//        val call = arduinoDeviceAPI.controllerWriteRequest(indexInArduinoServicesArray, value)
//
//        val controllerResponse = call.execute().body() // synchronous
//        if (controllerResponse != null) setNewState(controllerResponse.response)
//        return controllerResponse
//    }
//
//    override fun toString(): String {
//        return "ArduinoController{" +
//                "device guid=" + device.guid +
//                ", name=" + name +
//                ", guid=" + guid +
//                ", indexInArduinoServicesArray=" + indexInArduinoServicesArray +
//                ", type=" + type +
//                '}'.toString()
//    }
}
