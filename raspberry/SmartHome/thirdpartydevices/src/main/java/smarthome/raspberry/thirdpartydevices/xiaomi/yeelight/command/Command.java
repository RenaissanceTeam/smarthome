package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.command;

import com.google.gson.annotations.Expose;

import smarthome.raspberry.thirdpartydevices.utils.Utils;

public class Command {

    private static int ID_GENERATOR = 1;


    @Expose
    private int id;

    @Expose private String method;

    @Expose private Object[] params;


    private static int generateId() {
        return ID_GENERATOR++;
    }


    public Command(String method, Object... params) {
        this.id = Command.generateId();
        this.method = method;
        this.params = params;
    }

    public int getId() {
        return this.id;
    }

    public String toJson() {
        return Utils.Companion.getGSON().toJson(this);
    }

}
