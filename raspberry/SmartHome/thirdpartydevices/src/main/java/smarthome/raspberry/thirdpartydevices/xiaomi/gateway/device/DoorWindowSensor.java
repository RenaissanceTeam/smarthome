package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device;

import org.json.JSONException;
import org.json.JSONObject;

import static smarthome.library.common.constants.DeviceTypes.DOOR_WINDOW_SENSOR_TYPE;

public class DoorWindowSensor extends ExtendedDevice  {

    private OnStateChangeListener listener;

    public DoorWindowSensor(String sid) {
        super(sid, DOOR_WINDOW_SENSOR_TYPE);
    }

    public DoorWindowSensor(String sid, OnStateChangeListener listener) {
        this(sid);
        setListener(listener);
    }


    public void setListener(OnStateChangeListener listener) {
        this.listener = listener;
    }


    @Override
    public void parseData(String cmd) {
        try {
            JSONObject o = new JSONObject(cmd);

            if(!o.isNull(STATUS_KEY)) {
                setStatus(o.getString(STATUS_KEY));

                report(getStatus());
            }

            if(!o.isNull(VOLTAGE_KEY)) {
                String vol = o.getString(VOLTAGE_KEY);
                setVoltage(Float.parseFloat(vol) / 1000);
            }

        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
        }
    }


    private void report(String state) {
        if(listener != null)
            listener.onStateChanged(state);
    }

    public interface OnStateChangeListener {

        void onStateChanged(String state);
    }
}
