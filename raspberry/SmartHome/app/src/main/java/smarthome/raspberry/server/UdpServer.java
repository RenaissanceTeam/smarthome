package smarthome.raspberry.server;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import smarthome.raspberry.utils.ip.Helpers;

import static smarthome.raspberry.BuildConfig.DEBUG;


public class UdpServer implements StoppableServer {

    private static final String TAG = UdpServer.class.getSimpleName();
    private static final int PORT = 59743;
    private static final String ARDUINO_INIT_URL = "http://%s:8080/init";
    private static final String REMOTE_ADDR_HEADER = "Remote_Addr";

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    @Override
    public void startServer() {
        isRunning.set(true);
        startListening();
    }

    @Override
    public void stopServer() {
        isRunning.set(false);
    }

    private void startListening() {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                while (isRunning.get()) {
                    try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {

                        Log.d(TAG, "run: block to receive udp packet");
                        DatagramPacket receivePacket = listen(serverSocket); // blocking call
                        Log.d(TAG, "run: cancel blocking, received packet");

                        onReceiveFromUpd(receivePacket); // blocking call
                    } catch (IOException e) {
                        //Log.e(TAG, "", e);
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
        if (DEBUG) Log.d(TAG, "received: " + sentence);
        // todo how to get only valid info??
        // todo check if secured udp packet received
        // todo check if udp packet from iot device

        InetAddress ip = receivePacket.getAddress();

        Request request = new Request.Builder()
                .url(getArduinoInitUrl(ip))
                .header(REMOTE_ADDR_HEADER, Helpers.getLocalIpAddress())
                .build();


        Response response = new OkHttpClient()
                .newCall(request)
                .execute();
        Log.d(TAG, "onReceiveFromUpd: request=" + request + ", response=" + response);
        response.body().close(); // blocking call
    }

    private String getArduinoInitUrl(InetAddress address) {
        return String.format(ARDUINO_INIT_URL, address.getHostAddress());
    }
}
