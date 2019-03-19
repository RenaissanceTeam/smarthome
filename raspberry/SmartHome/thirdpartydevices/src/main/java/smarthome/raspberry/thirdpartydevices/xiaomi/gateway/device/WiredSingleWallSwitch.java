package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.WiredSingleWallSwitchCmd;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.utils.UdpTransport;

import static smarthome.library.common.constants.DeviceTypes.WIRED_SINGLE_WALL_SWITCH_TYPE;

public class WiredSingleWallSwitch extends Device {

    private String status = "idle";

    private boolean statusBin = false;

    private OnSwitchChangeListener listener;

    private UdpTransport transport;

    public WiredSingleWallSwitch(String sid, UdpTransport transport) {
        super(sid, WIRED_SINGLE_WALL_SWITCH_TYPE);

        this.transport = transport;
    }

    public void setStatus(String status) {
        this.status = status;

        if(status.equals(STATUS_ON))
            statusBin = true;

        else if(status.equals(STATUS_OFF))
            statusBin = false;
    }


    @Override
    public void parseData(String cmd) {
        try {
            JSONObject o = new JSONObject(cmd);

            if(!o.isNull(STATUS_KEY)) {
                setStatus(o.getString(STATUS_KEY));
                Optional.ofNullable(listener).ifPresent(listener -> listener.onSwitch(statusBin));
            }

        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return super.toString() + "\nstatus: " + status;
    }

    public void on() {
        sendCmd(STATUS_ON);
    }

    public void off() {
        sendCmd(STATUS_OFF);
    }

    private void sendCmd(String status) {
        try {
            transport.sendWriteCommand(getSid(), getType(), new WiredSingleWallSwitchCmd(status));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnSwitchChangeListener {

        void onSwitch(boolean on);

    }

}
