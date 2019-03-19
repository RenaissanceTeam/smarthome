package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.command;

import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Device;

public class SmartPlugCmd extends Command {

    private String status;

    public SmartPlugCmd(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{\"" + Device.STATUS_KEY + "\":\"" + status + "\"}";
    }
}
