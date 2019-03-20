package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces
interface Readable {
    /**
     * key (status), value
     */
    fun read(): String
}