package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command;

public class WriteCmd extends Command {

    private final String sid;
    private final String type;
    private final String data;

    public WriteCmd(String sid, String type, String data) {
        this.sid = sid;
        this.type = type;
        this.data = data;
    }

    @Override
    public String toString() {
        return "{\"cmd\":\"write\",\"model\":\"" + type + "\",\"sid\":\"" + sid + "\",\"data\":" + data + "}";
    }
}
