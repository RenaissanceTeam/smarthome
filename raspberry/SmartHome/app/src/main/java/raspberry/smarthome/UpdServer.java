package raspberry.smarthome;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import ru.smarthome.library.constants.Constants;

import static raspberry.smarthome.MainActivity.DEBUG;

public class UpdServer {

    public static final String TAG = UpdServer.class.getSimpleName();
    public static final int PORT = 8080;

    public void startListening() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket serverSocket = new DatagramSocket(PORT);

                    while (true) { // todo interruptions.
                        DatagramPacket receivePacket = listen(serverSocket);
                        onReceiveFromUpd(receivePacket);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "failed to listen for udp requests ", e);
                }
            }
        }).run();
    }

    private DatagramPacket listen(DatagramSocket serverSocket) throws IOException {
        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket); // blocking call
        return receivePacket;
    }

    private void onReceiveFromUpd(DatagramPacket receivePacket) {
        String sentence = new String(receivePacket.getData());
        if (DEBUG) Log.d(TAG, "received: " + sentence);
        // todo check if secured udp packet received
        // todo check if udp packet from iot device

        InetAddress ip = receivePacket.getAddress();
        int port = receivePacket.getPort();
        // todo http request to given credentials
    }

    public void send(String data) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress ip = InetAddress.getByName("localhost");

        byte[] sendData = data.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, PORT);
        clientSocket.send(sendPacket);
        clientSocket.close();
    }


}
