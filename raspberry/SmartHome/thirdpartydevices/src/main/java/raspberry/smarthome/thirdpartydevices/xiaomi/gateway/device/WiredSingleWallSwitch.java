package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class WiredSingleWallSwitch extends Device {

    private OnSwitchChangeListener listener;

    private String status = "idle";

    private boolean statustBin = false;

    public WiredSingleWallSwitch(String sid) {
        super(sid, WIRED_SINGLE_WALL_SWITCH_TYPE);
    }

    public void setStatus(String status) {
        this.status = status;

        if(status.equals(STATUS_ON))
            statustBin = true;

        else if(status.equals(STATUS_OFF))
            statustBin = false;
    }


    @Override
    public void parseData(String cmd) {
        try {
            JSONObject o = new JSONObject(cmd);

            if(!o.isNull(STATUS_KEY)) {
                setStatus(o.getString(STATUS_KEY));
                Optional.ofNullable(listener).ifPresent(listener -> listener.onSwitch(statustBin));
            }

        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return super.toString() + "\nstatus: " + status;
    }

    public interface OnSwitchChangeListener {

        void onSwitch(boolean on);

    }

}
