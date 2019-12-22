package smarthome.raspberry.arduinodevices.data.server

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean


class UdpServer {
    private val PORT = 59743
    private val ARDUINO_INIT_URL = "http://%s:8080/init"
    private val REMOTE_ADDR_HEADER = "Remote_Addr"
    private val isRunning = AtomicBoolean(false)
    
    fun startServer() {
        isRunning.set(true)
        Executors.newSingleThreadExecutor().submit{ startListening() }
    }
    
    fun stopServer() {
        isRunning.set(false)
    }

    private fun startListening() {
        while (isRunning.get()) {
            try {
                val packet = listen(DatagramSocket(PORT))
                onReceiveFromUpd(packet)
            } catch (e: IOException) {
                //Log.e(TAG, "", e);
            }
        }
    }

    private fun listen(serverSocket: DatagramSocket): DatagramPacket {
        val receiveData = ByteArray(1024)

        val receivePacket = DatagramPacket(receiveData, receiveData.size)
        serverSocket.receive(receivePacket)
        return receivePacket
    }

    private fun onReceiveFromUpd(receivePacket: DatagramPacket) {
        // todo how to get only valid info??
        // todo check if secured udp packet received
        // todo check if udp packet from iot device

        val ip = receivePacket.address

        val request = Request.Builder()
                .url(getArduinoInitUrl(ip))
                .header(REMOTE_ADDR_HEADER, Helpers.localIpAddress)
                .build()

        val response = OkHttpClient()
                .newCall(request)
                .execute()

        response.body().close()
    }

    private fun getArduinoInitUrl(address: InetAddress): String {
        return String.format(ARDUINO_INIT_URL, address.hostAddress)
    }
}