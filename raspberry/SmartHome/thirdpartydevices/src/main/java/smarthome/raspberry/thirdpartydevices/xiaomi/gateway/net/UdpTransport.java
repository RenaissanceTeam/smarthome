package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.utils.channel.IncomingMulticastChannel;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.Command;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.WriteCmd;

public class UdpTransport {

    private String gatewayIp = "224.0.0.50";
    private InetAddress multicastAdress = null;
    private static final int serverPort = 9898;
    private DatagramSocket socket = null;
    private IncomingMulticastChannel incomingMulticastChannel;

    private final String gatewayWritePassword;
    private final byte[] initVector =
            {0x17, (byte) 0x99, 0x6d, 0x09, 0x3d, 0x28, (byte) 0xdd, (byte) 0xb3, (byte) 0xba, 0x69, 0x5a, 0x2e, 0x6f, 0x58, 0x56, 0x2e};

    private String currentToken;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private final String TAG = getClass().getName();

    public UdpTransport() {
        this(null);
    }

    public UdpTransport(String gatewayWritePassword) {
        this.gatewayWritePassword = gatewayWritePassword;

        try {
            this.multicastAdress = InetAddress.getByName("224.0.0.50");
            this.socket = new DatagramSocket();
            this.socket.setBroadcast(true);

            this.incomingMulticastChannel = new IncomingMulticastChannel(multicastAdress, serverPort);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UdpTransport(String gatewayWritePassword, String gatewayIp) {
        this(gatewayWritePassword);
        setGatewayIp(gatewayIp);
    }

    public void setGatewayIp(String gatewayIp) {
        Log.i(TAG, "--- Gateway ip updated ---");
        this.gatewayIp = gatewayIp;
    }

    public void setCurrentToken(String currentToken) {
        this.currentToken = currentToken;
    }

    public IncomingMulticastChannel getIncomingMulticastChannel() {
        return incomingMulticastChannel;
    }

    private String getWriteKey(byte[] key, byte[] initVector) {
        byte[] encrypted = null;

        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            encrypted = cipher.doFinal(currentToken.getBytes(StandardCharsets.US_ASCII));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return toHexString(encrypted);
    }

    public void sendCommand(Command command) {
        Log.v(TAG,"Sending command: " + command.toString());

        sendCommand(command.toString());
    }

    private void sendCommand(String data) {
        byte[] buffer = data.getBytes(StandardCharsets.US_ASCII);

        executor.submit(() -> {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(gatewayIp), serverPort);
                socket.send(packet);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendWriteCommand(String sid, String type, Command cmd) throws Exception {
        if (gatewayWritePassword == null)
            throw new Exception("You cannot send commands to gateway without password");

        try {

            while (true) {
                if (currentToken == null) {
                    TimeUnit.MILLISECONDS.sleep(5000);
                    continue;
                }

                JSONObject o = new JSONObject(cmd.toString());

                o.put("key", getWriteKey(gatewayWritePassword.getBytes(StandardCharsets.US_ASCII), initVector));

                sendCommand(new WriteCmd(sid, type, "\"" + o.toString().replace("\"", "\\\"") + "\""));

                return;
            }
        } catch (Exception e) {
        }
    }

    public Future<String> receive() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        byte[] data = new byte[1024];

        try {
            DatagramPacket packet = new DatagramPacket(data, data.length);
            socket.receive(packet);

            //System.arraycopy(data, 0, buff, 0, packet.getLength());

            completableFuture.complete(new String(trimBytes(data), StandardCharsets.US_ASCII));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return completableFuture;
    }

    private byte[] trimBytes(byte[] data) {
        byte[] res;

        int i = 0;
        while (data[i] != 0)
            ++i;

        res = new byte[i];

        System.arraycopy(data, 0, res, 0, i);

        return res;
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        socket.close();
        super.finalize();
    }
}
