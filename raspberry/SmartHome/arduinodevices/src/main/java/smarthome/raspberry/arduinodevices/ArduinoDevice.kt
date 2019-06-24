//package smarthome.raspberry.arduinodevices
//
//import com.google.gson.Gson
//import com.google.gson.GsonBuilder
//import smarthome.library.common.IotDevice
//import smarthome.raspberry.arduinodevices.controllers.ArduinoController
//
//class ArduinoDevice(name: String,
//                    description: String,
//                    private val ip: String) : IotDevice(name, description) {
//
//    private val gson: Gson
//
//    init {
//        gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
//    }
//
//    fun getControllerByGuid(guid: Long): ArduinoController {
//        for (controller in controllers) {
//            if ((controller as ArduinoController).guid == guid) {
//                return controller
//            }
//        }
//        throw IllegalArgumentException("No controller with guid=$guid for iotDevice=$this")
//    }
//
//    fun gsonned(): String {
//        return gson.toJson(this)
//    }
//
//    override fun toString(): String {
//        return "ArduinoDevice{" +
//                "ip='" + ip + '\''.toString() +
//                ", controllers=" + controllers +
//                ", name='" + name + '\''.toString() +
//                ", description='" + description + '\''.toString() +
//                ", GUID=" + guid +
//                '}'.toString()
//    }
//}
