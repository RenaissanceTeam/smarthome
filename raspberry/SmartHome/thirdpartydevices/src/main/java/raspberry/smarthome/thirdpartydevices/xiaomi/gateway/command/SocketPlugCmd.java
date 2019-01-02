package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.command;

public class SocketPlugCmd extends Command {

    public static final String STATUS_ON = "on";
    public static final String STATUS_OFF = "off";

    private String status;

    public SocketPlugCmd(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{\"status\":\"" + status + "\"}";
    }
}
