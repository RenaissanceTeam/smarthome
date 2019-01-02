package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class MotionSensor extends ExtendedDevice {

    private OnMotionListener listener;

    public int noMotion;

    private LocalDateTime motionDateTime;

    public MotionSensor(String sid) {
        super(sid, MOTION_SENSOR_TYPE);
    }

    public MotionSensor(String sid, OnMotionListener listener) {
        this(sid);
        setListener(listener);
    }

    public void setListener(OnMotionListener listener) {
        this.listener = listener;
    }

    public LocalDateTime getMotionDateTime() {
        return motionDateTime;
    }

    @Override
    public void parseData(String cmd) {
        try {
            JSONObject o = new JSONObject(cmd);

            if (!o.isNull(STATUS_KEY)) {
                setStatus(o.getString(STATUS_KEY));

                if (getStatus().equals(STATUS_MOTION)) {
                    motionDateTime = LocalDateTime.now();
                    report(STATUS_MOTION);
                }
            }

            if (!o.isNull(STATUS_NO_MOTION)) {
                noMotion = Integer.parseInt(o.getString(STATUS_NO_MOTION));
                report(STATUS_NO_MOTION);
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

        if(status.equals(STATUS_MOTION))
            listener.onMotion();

        else if(status.equals(STATUS_NO_MOTION))
            listener.onNoMotion(noMotion);
    }

    @Override
    public String toString() {
        return super.toString() + "\nlast motion datetime: " + motionDateTime.toString() + ", no motion for: " + noMotion + " seconds";
    }

    public interface OnMotionListener {

        void onMotion();

        void onNoMotion(int seconds);
    }
}
