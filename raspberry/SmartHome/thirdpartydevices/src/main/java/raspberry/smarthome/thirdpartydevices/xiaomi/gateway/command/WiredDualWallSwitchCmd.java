package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.command;

import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device.Device;

public class WiredDualWallSwitchCmd extends Command {

    private final String channel_0_status;
    private final String channel_1_status;

    public WiredDualWallSwitchCmd(String channel_0_status, String channel_1_status) {
        this.channel_0_status = channel_0_status;
        this.channel_1_status = channel_1_status;
    }

    @Override
    public String toString() {
        return "{\"" + Device.STATUS_CHANNEL_0 + "\":\"" + channel_0_status + ",\"" + Device.STATUS_CHANNEL_1 + "\":" + channel_1_status + "\"}";
    }
}
