package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import raspberry.smarthome.thirdpartydevices.R;

public class WeatherSensor extends THSensor {

    private OnPressureChangeListener listener;

    private Float pressure = null;

    public WeatherSensor(String sid) {
        super(sid, WEATHER_SENSOR_TYPE);
    }

    public void setListener(OnPressureChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void parseData(String cmd) {
        super.parseData(cmd);

        try {
            JSONObject o = new JSONObject(cmd);

            if(!o.isNull(STATUS_PRESSURE)) {
                float currPress = Float.parseFloat(o.getString(STATUS_PRESSURE)) / 100;

                if(pressure != null && Math.abs(currPress - pressure) > 0.01) {
                    Optional.ofNullable(listener).ifPresent(onPressureChangeListener -> onPressureChangeListener.onPressureChanged(currPress));
                }

                pressure = currPress;
            }

        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return super.toString() + "pressure: " + pressure + "kPA";
    }

    @Override
    public Bitmap getDevicePicture(Resources resources) {
        return BitmapFactory.decodeResource(resources, R.drawable.xiaomi_thp_sensor);
    }

    public interface OnPressureChangeListener {

        void onPressureChanged(float p);
    }
}
