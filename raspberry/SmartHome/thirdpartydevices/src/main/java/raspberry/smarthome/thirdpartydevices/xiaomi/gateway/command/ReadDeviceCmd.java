package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.command;

public class ReadDeviceCmd extends Command {

    private final String sid;

    public ReadDeviceCmd(String sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "{\"cmd\":\"read\",\"sid\":\"" + sid + "\"}";
    }
}
