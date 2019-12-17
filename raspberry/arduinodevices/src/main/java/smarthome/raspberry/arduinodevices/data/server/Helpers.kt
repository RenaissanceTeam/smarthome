package smarthome.raspberry.arduinodevices.data.server

import com.google.gson.Gson
import fi.iki.elonen.NanoHTTPD
import smarthome.library.common.util.Holder
import smarthome.raspberry.arduinodevices.data.server.entity.BadParams
import java.io.IOException
import java.net.Inet4Address
import java.net.NetworkInterface
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

object Helpers {

    val localIpAddress: String
        get() {
            val interfaces = NetworkInterface.getNetworkInterfaces()

            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val inetAddresses = networkInterface.inetAddresses

                while (inetAddresses.hasMoreElements()) {
                    val inetAddress = inetAddresses.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
            throw IOException("No local ip address")
        }
}

fun NanoHTTPD.IHTTPSession.body(): String {
    val map = HashMap<String, String>()
    parseBody(map)
    return map["postData"].orEmpty()
}

fun Map<String,String>.takeIfNotEmpty(name: String): String {
    return this[name]?.takeIf { it.isNotEmpty() } ?: throw BadParams("passed bad params=$this")
}