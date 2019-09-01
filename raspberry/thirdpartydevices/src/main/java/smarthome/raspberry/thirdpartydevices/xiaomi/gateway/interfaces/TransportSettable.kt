package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.interfaces

import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport

interface TransportSettable {
    fun setUpTransport(transport: UdpTransport)
}