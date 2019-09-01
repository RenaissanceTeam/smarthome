package smarthome.raspberry.thirdpartydevices.network

import android.util.Log
import smarthome.raspberry.thirdpartydevices.BuildConfig.DEBUG
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.net.Socket

class SocketHolder(private val ip: String, private val port: Int) {
    private val TAG = javaClass.name

    private val timeout: Int = 1000

    private val socket: Socket

    private val reader: BufferedReader
    private val writer: BufferedWriter

    init {
        val inetAddress = InetSocketAddress(ip, port)
        socket = Socket()
        socket.connect(inetAddress, timeout)
        socket.soTimeout = timeout

        reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
    }

    fun send (data: String) {
        if (DEBUG) Log.d(TAG, "sending $data to $ip:$port")
        writer.write(data)
        writer.flush()
    }

    fun read (): String {
        if (DEBUG) Log.d(TAG, "--------------------\nreading from $ip:$port")
        val data: String = reader.readLine()
        if (DEBUG) Log.d(TAG, "--------------------\ndata received:\n$data")
        return data
    }
}