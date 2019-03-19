package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.utils.channel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class DirectChannel {

    private String ip;
    private int port;
    private DatagramSocket socket;

    public String getIp() {
        return ip;
    }

    public DirectChannel(String destnationIp, int destinationPort) throws IOException {
        this.ip = destnationIp;
        this.port = destinationPort;
        this.socket = new DatagramSocket();
    }

    public byte[] receive() throws IOException {
        byte buffer[] = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        int responseLength = packet.getLength();
        byte response[] = new byte[responseLength];
        System.arraycopy(buffer, 0, response, 0, responseLength);
        return response;
    }

    public void send(byte[] bytes) throws IOException {
        InetSocketAddress address = new InetSocketAddress(ip, port);
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address);
        socket.send(packet);
    }
}
