package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces
interface GatewayReadable {
    /**
     * key (status), value
     */
    fun read(): String
}