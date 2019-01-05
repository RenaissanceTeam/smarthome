package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device;

import org.json.JSONException;
import org.json.JSONObject;

import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.command.GatewayLightCmd;
import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.utils.UdpTransport;

public class Gateway extends Device {

    private UdpTransport udpTransport;

    private String ip = "224.0.0.50";

    private String rgb;
    private String illumination;
    private String protoVersion;

    public Gateway(String sid, UdpTransport transport) {
        super(sid, GATEWAY_TYPE);
        udpTransport = transport;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;

        udpTransport.setGatewayIp(ip);
    }

    public String getRgb() {
        return rgb;
    }

    public String getIllumination() {
        return illumination;
    }

    public String getProtoVersion() {
        return protoVersion;
    }

    @Override
    public String toString() {
        return super.toString() + "\nrgb: " + rgb + ", illumination: " + illumination + ", proto version: " + protoVersion;
    }

    @Override
    public void parseData(String cmd) {
        try {
            JSONObject o = new JSONObject(cmd);

            if(!o.isNull(IP_KEY))
                setIp(o.getString(IP_KEY));

            if(!o.isNull(RGB_KEY))
                rgb = o.getString(RGB_KEY);

            if(!o.isNull(ILLUMINATION_KEY))
                illumination = o.getString(ILLUMINATION_KEY);

            if(!o.isNull(PROTO_VERSION_KEY))
                protoVersion = o.getString(PROTO_VERSION_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void enableLight() {
        enableLight((byte) 255, (byte) 255, (byte) 255, 1000);
    }

    public void enableLight(byte r, byte g, byte b, int illumination) {
        long rgb = 0xFF000000 | r << 16 | g << 8 | b;

        if (illumination < 300 || illumination > 1300) throw new IllegalArgumentException("Illumination must be in range 300 - 1300");

        try {
            udpTransport.sendWriteCommand(getSid(), getType(), new GatewayLightCmd(rgb, illumination));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disableLight() {
        try {
            udpTransport.sendWriteCommand(getSid(), getType(), new GatewayLightCmd(0, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
