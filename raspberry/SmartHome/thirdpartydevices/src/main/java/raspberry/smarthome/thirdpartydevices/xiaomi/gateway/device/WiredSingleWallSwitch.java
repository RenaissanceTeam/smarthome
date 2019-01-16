package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import raspberry.smarthome.thirdpartydevices.R;
import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.command.WiredSingleWallSwitchCmd;
import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.utils.UdpTransport;

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

    @Override
    public Bitmap getDevicePicture(Resources resources) {
        return BitmapFactory.decodeResource(resources, R.drawable.xiaomi_wired_single_wall_switch);
    }

    public interface OnSwitchChangeListener {

        void onSwitch(boolean on);

    }

}