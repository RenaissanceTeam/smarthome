package raspberry.smarthome.server;

import android.text.format.Formatter;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Retrofit;

import static raspberry.smarthome.MainActivity.DEBUG;

public class UdpServer implements StoppableServer {

    public static final String TAG = UdpServer.class.getSimpleName();
    public static final int PORT = 59743;

    public void startListening() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    DatagramSocket serverSocket = null;
                    try {
                        serverSocket = new DatagramSocket(PORT);

                        DatagramPacket receivePacket = listen(serverSocket); // blocking call
                        serverSocket.close();
                        serverSocket = null;

                        onReceiveFromUpd(receivePacket); // blocking call
                    } catch (IOException e) {
                        Log.e(TAG, "failed to listen for udp requests ", e);
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
        int port = receivePacket.getPort();


        Request request = new Request.Builder()
                .url("http://" + ip.getHostAddress() + ":8080" + "/init")
                .header("Remote_Addr", getLocalIpAddress())
                .build();

        new OkHttpClient().newCall(request).execute(); // blocking call
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
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
        startListening();
    }

    @Override
    public void stopServer() {
    }
}
