package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.command;

import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device.Device;

public class WiredSingleWallSwitchCmd extends Command {

    private String status;

    public WiredSingleWallSwitchCmd(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{\"" + Device.STATUS_KEY + "\":\"" + status + "\"}";
    }
}
