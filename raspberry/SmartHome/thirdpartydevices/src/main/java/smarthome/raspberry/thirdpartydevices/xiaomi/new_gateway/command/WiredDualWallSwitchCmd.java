package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.command;

import static smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Device.STATUS_CHANNEL_0;
import static smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Device.STATUS_CHANNEL_1;

public class WiredDualWallSwitchCmd extends Command {

    private final String channel_0_status;
    private final String channel_1_status;

    public WiredDualWallSwitchCmd(String channel_0_status, String channel_1_status) {
        this.channel_0_status = channel_0_status;
        this.channel_1_status = channel_1_status;
    }

    @Override
    public String toString() {
        return "{\"" + STATUS_CHANNEL_0 + "\":\"" + channel_0_status + ",\"" + STATUS_CHANNEL_1 + "\":" + channel_1_status + "\"}";
    }
}
