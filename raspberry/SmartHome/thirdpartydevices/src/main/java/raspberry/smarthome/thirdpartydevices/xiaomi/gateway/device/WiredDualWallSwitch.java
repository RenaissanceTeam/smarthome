package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class WiredDualWallSwitch extends Device {

    private OnSwitchChannelChangeListener listener;

    private String statusLeft = "idle";
    private String statusRight = "idle";

    private boolean statusLeftBin = false;
    private boolean statusRightBin = false;

    public WiredDualWallSwitch(String sid) {
        super(sid, WIRED_DUAL_WALL_SWITCH_TYPE);
    }

    public void setStatusLeft(String statusLeft) {
        this.statusLeft = statusLeft;

        if(statusLeft.equals(STATUS_ON))
            statusLeftBin = true;

        else if(statusLeft.equals(STATUS_OFF))
            statusLeftBin = false;
    }

    public void setStatusRight(String statusRight) {
        this.statusRight = statusRight;

        if(statusRight.equals(STATUS_ON))
            statusRightBin = true;

        else if(statusRight.equals(STATUS_OFF))
            statusRightBin = false;
    }

    @Override
    public void parseData(String cmd) {
        try {
            JSONObject o = new JSONObject(cmd);

            if(!o.isNull(STATUS_CHANNEL_0)) {
                setStatusLeft(o.getString(STATUS_CHANNEL_0));
                Optional.ofNullable(listener).ifPresent(listener -> listener.onSwitchlLeft(statusLeftBin));
            }

            if(!o.isNull(STATUS_CHANNEL_1)) {
                setStatusRight(o.getString(STATUS_CHANNEL_1));
                Optional.ofNullable(listener).ifPresent(listener -> listener.onSwitchRight(statusRightBin));
            }

        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return super.toString() + "\nstatus left: " + statusLeft + ", status right: " + statusRight;
    }

    public interface OnSwitchChannelChangeListener {

        void onSwitchlLeft(boolean on);

        void onSwitchRight(boolean on);
    }
}
