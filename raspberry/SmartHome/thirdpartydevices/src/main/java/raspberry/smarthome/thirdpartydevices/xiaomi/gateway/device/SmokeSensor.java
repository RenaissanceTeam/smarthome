package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import raspberry.smarthome.thirdpartydevices.R;

public class SmokeSensor extends ExtendedDevice {

    private boolean alarm;

    private float density;

    private AlarmListener alarmListener;
    private DensityListener densityListener;

    public SmokeSensor(String sid) {
        super(sid, SMOKE_SENSOR_TYPE);
    }

    public SmokeSensor(String sid, String type, AlarmListener alarmListener) {
        super(sid, type);
        this.alarmListener = alarmListener;
    }

    public SmokeSensor(String sid, String type, AlarmListener alarmListener, DensityListener densityListener) {
        super(sid, type);
        this.alarmListener = alarmListener;
        this.densityListener = densityListener;
    }

    public void setAlarmListener(AlarmListener alarmListener) {
        this.alarmListener = alarmListener;
    }

    public void setDensityListener(DensityListener densityListener) {
        this.densityListener = densityListener;
    }

    @Override
    public void parseData(String cmd) {
        try {
            JSONObject o = new JSONObject(cmd);

            if(!o.isNull(VOLTAGE_KEY)) {
                String vol = o.getString(VOLTAGE_KEY);
                setVoltage(Float.parseFloat(vol) / 1000);
            }

            if (!o.isNull(ALARM_KEY)) {
                boolean condition = Boolean.parseBoolean(o.getString(ALARM_KEY));

                if(condition)
                    Optional.ofNullable(alarmListener).ifPresent(AlarmListener::onAlarm);

                else
                    Optional.ofNullable(alarmListener).ifPresent(AlarmListener::onAlarmStopped);

                alarm = condition;
            }

            if (!o.isNull(DENSITY_KEY)) {
                float d = Float.parseFloat(o.getString(DENSITY_KEY)) / 100;

                if(Math.abs(d - density) > 0.01)
                    Optional.ofNullable(densityListener).ifPresent(listener -> listener.onDensityChanged(d));

                density = d;
            }

        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return super.toString() + "\nalarm: " + (alarm ? "on" : "off") + ", density: " + density;
    }

    @Override
    public Bitmap getDevicePicture(Resources resources) {
        return BitmapFactory.decodeResource(resources, R.drawable.xiaomi_smoke_sensor);
    }

    public interface AlarmListener {

        void onAlarm();

        void onAlarmStopped();

    }

    public interface DensityListener {

        void onDensityChanged(float density);

    }
}
