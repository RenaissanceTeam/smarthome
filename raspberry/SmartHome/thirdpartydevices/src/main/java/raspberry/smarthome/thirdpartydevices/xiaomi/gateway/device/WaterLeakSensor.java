package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import raspberry.smarthome.thirdpartydevices.R;

public class WaterLeakSensor extends ExtendedDevice {

    private OnWaterLeakListener listener;

    public WaterLeakSensor(String sid) {
        super(sid, WATER_LEAK_SENSOR_TYPE);
    }

    public WaterLeakSensor(String sid, OnWaterLeakListener listener) {
        this(sid);
        setListener(listener);
    }

    public void setListener(OnWaterLeakListener listener) {
        this.listener = listener;
    }

    @Override
    public void parseData(String cmd) {
        try {
            JSONObject o = new JSONObject(cmd);

            if(!o.isNull(STATUS_KEY)) {
                setStatus(o.getString(STATUS_KEY));

                if(getStatus().equals(STATUS_WATER_LEAK))
                    Optional.ofNullable(listener).ifPresent(OnWaterLeakListener::onWaterLeak);

                else if(getStatus().equals(STATUS_NO_WATER_LEAK))
                    Optional.ofNullable(listener).ifPresent(OnWaterLeakListener::onNoWaterLeak);
            }

            if(!o.isNull(VOLTAGE_KEY)) {
                String vol = o.getString(VOLTAGE_KEY);
                setVoltage(Float.parseFloat(vol) / 1000);
            }

        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap getDevicePicture(Resources resources) {
        return BitmapFactory.decodeResource(resources, R.drawable.xiaomi_water_leak_sensor);
    }

    public interface OnWaterLeakListener {

        void onWaterLeak();

        void onNoWaterLeak();
    }
}