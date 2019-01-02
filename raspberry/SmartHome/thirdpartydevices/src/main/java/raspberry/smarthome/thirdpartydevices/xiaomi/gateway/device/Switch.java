package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device;

import org.json.JSONException;
import org.json.JSONObject;

public class Switch extends ExtendedDevice {

    private OnSwitchListener listener;

    public Switch(String sid) {
        super(sid, SWITCH_TYPE);
    }

    public Switch(String sid, OnSwitchListener listener) {
        this(sid);
        setListener(listener);
    }

    public void setListener(OnSwitchListener listener) {
        this.listener = listener;
    }

    @Override
    public void parseData(String cmd) {
        try {
            JSONObject o = new JSONObject(cmd);

            if (!o.isNull(STATUS_KEY)) {
                setStatus(o.getString(STATUS_KEY));

                if (getStatus().equals(STATUS_CLICK))
                    report(STATUS_CLICK);

                if (getStatus().equals(STATUS_DOUBLE_CLICK))
                    report(STATUS_DOUBLE_CLICK);

                if (getStatus().equals(STATUS_LONG_PRESS))
                    report(STATUS_LONG_PRESS);

            }

            if (!o.isNull(VOLTAGE_KEY)) {
                String vol = o.getString(VOLTAGE_KEY);
                setVoltage(Float.parseFloat(vol) / 1000);
            }

        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void report(String status) {
        if(listener == null) return;

        if(status.equals(STATUS_CLICK))
            listener.onClick();

        else if(status.equals(STATUS_DOUBLE_CLICK))
            listener.onDoubleClick();

        else if(status.equals(STATUS_LONG_PRESS))
            listener.onLongPress();

    }

    public interface OnSwitchListener {

        void onClick();

        void onDoubleClick();

        void onLongPress();
    }
}
