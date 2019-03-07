package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command;

import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Device;

public class WirelessSwitchCmd extends Command {

    private String status;

    public WirelessSwitchCmd(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{\"" + Device.STATUS_KEY + "\":\"" + status + "\"}";
    }
}
