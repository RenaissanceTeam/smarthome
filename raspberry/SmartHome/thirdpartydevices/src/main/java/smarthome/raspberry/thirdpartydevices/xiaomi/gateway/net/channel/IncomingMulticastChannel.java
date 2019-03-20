package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.channel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class IncomingMulticastChannel {

    private MulticastSocket socket;

    public IncomingMulticastChannel (InetAddress group, int port) throws IOException {
        socket = new MulticastSocket(port);
        socket.joinGroup(group);
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

}
