package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.command;

public class ResponseCmd {

    public String cmd;

    public String model;

    public String sid;

    public String token;

    public String data;


    @Override
    public String toString() {
        return "cmd: " + cmd + ", model: " + model + ", sid: " + sid + ", token: " + token + ", data: " + data;
    }
}
