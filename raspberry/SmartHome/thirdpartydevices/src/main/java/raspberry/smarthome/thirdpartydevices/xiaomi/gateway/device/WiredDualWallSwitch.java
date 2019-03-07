package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.command.WiredDualWallSwitchCmd;
import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.utils.UdpTransport;

import static smarthome.library.common.constants.DeviceTypes.WIRED_DUAL_WALL_SWITCH_TYPE;

public class WiredDualWallSwitch extends Device {

    private String statusLeft = "idle";
    private String statusRight = "idle";

    private boolean statusLeftBin = false;
    private boolean statusRightBin = false;

    private OnSwitchChannelChangeListener listener;

    private UdpTransport transport;

    public WiredDualWallSwitch(String sid, UdpTransport transport) {
        super(sid, WIRED_DUAL_WALL_SWITCH_TYPE);

        this.transport = transport;
    }

    private void setStatusLeft(String statusLeft) {
        this.statusLeft = statusLeft;

        if(statusLeft.equals(STATUS_ON))
            statusLeftBin = true;

        else if(statusLeft.equals(STATUS_OFF))
            statusLeftBin = false;
    }

    private void setStatusRight(String statusRight) {
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
                Optional.ofNullable(listener).ifPresent(listener -> listener.onSwitchLeft(statusLeftBin));
            }

            if(!o.isNull(STATUS_CHANNEL_1)) {
                setStatusRight(o.getString(STATUS_CHANNEL_1));
                Optional.ofNullable(listener).ifPresent(listener -> listener.onSwitchRight(statusRightBin));
            }

        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void onLeft() {
        sendCmd(STATUS_ON, statusRight);
    }

    public void offLeft() {
        sendCmd(STATUS_OFF, statusRight);
    }

    public void onRight() {
        sendCmd(statusLeft, STATUS_ON);
    }

    public void offRight() {
        sendCmd(statusLeft, STATUS_OFF);
    }

    private void sendCmd(String statusLeft, String statusRight) {
        try {
            transport.sendWriteCommand(getSid(), getDeviceType(), new WiredDualWallSwitchCmd(statusLeft, statusRight));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return super.toString() + "\nstatus left: " + statusLeft + ", status right: " + statusRight;
    }

    public interface OnSwitchChannelChangeListener {

        void onSwitchLeft(boolean on);

        void onSwitchRight(boolean on);
    }
}
