package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.discover

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.model.DiscoverResponse
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.net.SocketTimeoutException

class DeviceDetector {
    private val adress: InetSocketAddress = InetSocketAddress(DISCOVER_REQUEST_ADDRESS, DISCOVER_REQUEST_PORT)
    private val requestPack: DatagramPacket = DatagramPacket(requestBytes, requestBytes.size, adress)
    private val responsePack: DatagramPacket = DatagramPacket(buffer, buffer.size)
    private val socket: DatagramSocket = DatagramSocket()

    init {
        socket.soTimeout = SOCKET_TIMEOUT
    }

    fun discover(): Set<YeelightDevice> {
        val devices: MutableSet<YeelightDevice> = HashSet()

        socket.send(requestPack)

        try {
            while (true) {
                socket.receive(responsePack)

                val device: YeelightDevice = createDevice(String(buffer, 0, responsePack.length))
                devices.add(device)
            }
        } catch (e: SocketTimeoutException) { }

        return devices
    }

    private fun createDevice(response: String): YeelightDevice {
        val data: List<String> = response.split("\r\n")

        val discoverResponse = DiscoverResponse()

        for (line in data) {
            when {
                line.startsWith(LOCATION_PREFIX) -> {
                    val ipPort = line.substring(LOCATION_PREFIX.length).split(":")
                    discoverResponse.ip = ipPort[0]
                    discoverResponse.port = ipPort[1].toInt()
                }
                line.startsWith(ID_PREFIX) -> discoverResponse.id = line.substring(ID_PREFIX.length)
                line.startsWith(MODEL_PREFIX) -> discoverResponse.model = line.substring(MODEL_PREFIX.length)
                line.startsWith(FIRMWARE_PREFIX) -> discoverResponse.fw_ver = line.substring(FIRMWARE_PREFIX.length)
                line.startsWith(SUPPORT_PREFIX) -> discoverResponse.support = line.substring(SUPPORT_PREFIX.length).split(" ")
                line.startsWith(POWER_PREFIX) -> discoverResponse.power = line.substring(POWER_PREFIX.length)
                line.startsWith(BRIGHTNESS_PREFIX) -> discoverResponse.bright = line.substring(BRIGHTNESS_PREFIX.length).toInt()
                line.startsWith(COLOR_PREFIX) -> discoverResponse.color_mode = line.substring(COLOR_PREFIX.length).toInt()
                line.startsWith(COLOR_TEMPERATURE_PREFIX) -> discoverResponse.ct = line.substring(COLOR_TEMPERATURE_PREFIX.length).toInt()
                line.startsWith(RGB_PREFIX) -> discoverResponse.rgb = line.substring(RGB_PREFIX.length).toInt()
                line.startsWith(HUE_PREFIX) -> discoverResponse.hue = line.substring(HUE_PREFIX.length).toInt()
                line.startsWith(SATURATION_PREFIX) -> discoverResponse.sat = line.substring(SATURATION_PREFIX.length).toInt()
                line.startsWith(NAME_PREFIX) -> discoverResponse.name = line.substring(NAME_PREFIX.length)
            }
        }

        return YeelightDevice(discoverResponse)
    }

    companion object {
        const val LOCATION_PREFIX = "Location: yeelight://"
        const val ID_PREFIX = "id: "
        const val MODEL_PREFIX = "model: "
        const val FIRMWARE_PREFIX = "fw_ver: "
        const val SUPPORT_PREFIX = "support: "
        const val POWER_PREFIX = "power: "
        const val BRIGHTNESS_PREFIX = "bright: "
        const val COLOR_PREFIX = "color_mode: "
        const val COLOR_TEMPERATURE_PREFIX = "ct: "
        const val RGB_PREFIX = "rgb: "
        const val HUE_PREFIX = "hue: "
        const val SATURATION_PREFIX = "sat: "
        const val NAME_PREFIX = "name: "

        const val DISCOVER_REQUEST_ADDRESS = "239.255.255.250"
        const val DISCOVER_REQUEST_PORT = 1982
        const val SOCKET_TIMEOUT = 1000

        const val request = "M-SEARCH * HTTP/1.1"    + "\r\n" +
                            "MAN: \"ssdp:discover\"" + "\r\n" +
                            "ST: wifi_bulb"          + "\r\n"

        val requestBytes: ByteArray = request.toByteArray()
        var buffer: ByteArray = ByteArray(2000)

        private var instance: DeviceDetector? = null

        fun getInstance(): DeviceDetector {
            if (instance == null)
                instance = DeviceDetector()

            return instance as DeviceDetector
        }
    }
}