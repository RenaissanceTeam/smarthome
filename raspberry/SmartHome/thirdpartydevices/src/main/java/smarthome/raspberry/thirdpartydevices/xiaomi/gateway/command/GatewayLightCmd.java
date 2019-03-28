package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command;

public class GatewayLightCmd extends Command {

    private final long rgb;
    private final int illumination;

    public GatewayLightCmd(long rgb, int illumination) {
        this.rgb = rgb;
        this.illumination = illumination;
    }

    @Override
    public String toString() {
        return "{\"rgb\":" + rgb + ",\"illumination\":" + illumination + "}";
    }
}
