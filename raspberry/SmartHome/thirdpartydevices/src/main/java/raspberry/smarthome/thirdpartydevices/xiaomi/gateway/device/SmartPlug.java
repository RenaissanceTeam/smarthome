package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.command.SmartPlugCmd;
import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.utils.UdpTransport;

import static smarthome.library.common.constants.DeviceTypes.SMART_PLUG_TYPE;

public class SmartPlug extends ExtendedDevice {

    private int inUse;

    private int powerConsumed;

    private float loadPower;

    private OnStateChangeListener listener;

    private UdpTransport transport;

    public SmartPlug(String sid, UdpTransport transport) {
        super(sid, SMART_PLUG_TYPE);

        this.transport = transport;
    }

    public void setListener(OnStateChangeListener listener) {
        this.listener = listener;
    }

    public int getInUse() {
        return inUse;
    }

    public int getPowerConsumed() {
        return powerConsumed;
    }

    public float getLoadPower() {
        return loadPower;
    }

    @Override
    public void parseData(String cmd) {
        try {
            JSONObject o = new JSONObject(cmd);

            if(!o.isNull(VOLTAGE_KEY)) {
                String vol = o.getString(VOLTAGE_KEY);
                setVoltage(Float.parseFloat(vol) / 1000);
            }

            if (!o.isNull(STATUS_KEY)) {
                setStatus(o.getString(STATUS_KEY));
                Optional.ofNullable(listener).ifPresent(l -> l.onStateChanged(getStatus()));
            }

            if (!o.isNull(IN_USE_KEY)) {
                inUse = Integer.parseInt(o.getString(IN_USE_KEY));
            }

            if (!o.isNull(POWER_CONSUMED_KEY)) {
                powerConsumed = Integer.parseInt(o.getString(POWER_CONSUMED_KEY));
            }

            if (!o.isNull(LOAD_POWER_KEY)) {
                loadPower = Integer.parseInt(o.getString(LOAD_POWER_KEY));
            }

        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return super.toString() + "\nin use: " + inUse + ", power consumed: " + powerConsumed + "w, load power: " + loadPower + "v";
    }

    public void on() {
        sendCmd(STATUS_ON);
    }

    public void off() {
        sendCmd(STATUS_OFF);
    }

    private void sendCmd(String status) {
        try {
            transport.sendWriteCommand(getSid(), getDeviceType(), new SmartPlugCmd(status));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnStateChangeListener {

        void onStateChanged(String state);
    }
}
