package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces
interface Readable {
    /**
     * key (status), value
     */
    fun read(): String
}