package ru.smarthome.server;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.smarthome.utils.ip.Helpers;

import static ru.smarthome.MainActivity.DEBUG;

public class UdpServer implements StoppableServer {

    public static final String TAG = UdpServer.class.getSimpleName();
    public static final int PORT = 59743;
    public static final String ARDUINO_INIT_URL = "http://%s:8080/init";
    public static final int THREAD_POOL_SIZE = 4;
    public static final String REMOTE_ADDR_HEADER = "Remote_Addr";

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    public void startListening() {
        // todo does it really gives 4 parallel connections?
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (isRunning.get()) {
                    DatagramSocket serverSocket = null;
                    try {
                        serverSocket = new DatagramSocket(PORT);

                        DatagramPacket receivePacket = listen(serverSocket); // blocking call
                        serverSocket.close();
                        serverSocket = null;

                        onReceiveFromUpd(receivePacket); // blocking call
                    } catch (IOException e) {
                        Log.e(TAG, "", e);
                    } finally {
                        if (serverSocket != null) {
                            serverSocket.close();
                        }
                    }
                }
            }
        });
    }

    private DatagramPacket listen(DatagramSocket serverSocket) throws IOException {
        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);
        return receivePacket;
    }

    private void onReceiveFromUpd(DatagramPacket receivePacket) throws IOException {
        String sentence = new String(receivePacket.getData());
        if (DEBUG) Log.d(TAG, "received: " + sentence); // todo how to get only valid info??
        // todo check if secured udp packet received
        // todo check if udp packet from iot device

        InetAddress ip = receivePacket.getAddress();

        Request request = new Request.Builder()
                .url(getArduinoInitUrl(ip))
                .header(REMOTE_ADDR_HEADER, Helpers.getLocalIpAddress())
                .build();

        new OkHttpClient().newCall(request).execute(); // blocking call // todo make with retrofit
    }

    private String getArduinoInitUrl(InetAddress address) {
        return String.format(ARDUINO_INIT_URL, address.getHostAddress());
    }

    public void send(String data) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress ip = InetAddress.getByName("localhost");

        byte[] sendData = data.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, PORT);
        clientSocket.send(sendPacket);
        clientSocket.close();
    }

    @Override
    public void startServer() {
        isRunning.set(true);
        startListening();
    }

    @Override
    public void stopServer() {
        isRunning.set(false);
    }
}
