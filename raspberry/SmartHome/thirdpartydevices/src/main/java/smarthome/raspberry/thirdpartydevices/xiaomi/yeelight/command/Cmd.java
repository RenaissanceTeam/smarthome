package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.command;

import com.google.gson.annotations.Expose;

public class Cmd {

    private static int ID_GENERATOR = 1;


    @Expose
    private int id;

    @Expose private String method;

    @Expose private Object[] params;


    private static int generateId() {
        return ID_GENERATOR++;
    }


    public Cmd(String method, Object... params) {
        this.id = Cmd.generateId();
        this.method = method;
        this.params = params;
    }

    public int getId() {
        return this.id;
    }

    public String toJson() {
        return null; //TODO: implement
    }

}
