package smarthome.raspberry.arduinodevices.server

import com.google.gson.Gson
import fi.iki.elonen.NanoHTTPD
import java.io.IOException
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Enumeration

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

inline fun <reified T>NanoHTTPD.IHTTPSession.body(): T {
    val map = HashMap<String, String>()
    parseBody(map)
    return Gson().fromJson(map["postData"], T::class.java)
}
