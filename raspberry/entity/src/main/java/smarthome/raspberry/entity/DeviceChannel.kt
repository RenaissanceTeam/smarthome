package smarthome.raspberry.entity

interface DeviceChannel {
    fun canWorkWith(type: String): Boolean
    fun read(controller: Controller): String
    fun write(controller: Controller, state: String): String
}