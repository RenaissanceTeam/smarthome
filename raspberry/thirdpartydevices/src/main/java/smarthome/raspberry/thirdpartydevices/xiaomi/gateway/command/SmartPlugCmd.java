package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command;

import static smarthome.library.common.constants.ControllerStatusesKt.STATUS_KEY;

public class SmartPlugCmd extends Command {

    private String status;

    public SmartPlugCmd(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{\"" + STATUS_KEY + "\":\"" + status + "\"}";
    }
}
