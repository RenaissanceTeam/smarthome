package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import raspberry.smarthome.thirdpartydevices.R;

public class THSensor extends ExtendedDevice {

    private OnTemperatureChangeListener onTemperatureChangeListener;
    private OnHumidityChangeListener onHumidityChangeListener;

    private Float temperature = null;
    private Float humidity = null;

    public THSensor(String sid) {
        super(sid, TEMPERATURE_HUMIDITY_SENSOR_TYPE);
    }

    public THSensor(String sid, String type) {
        super(sid, type);
    }

    public THSensor(String sid, OnTemperatureChangeListener onTemperatureChangeListener) {
        this(sid);
        setOnTemperatureChangeListener(onTemperatureChangeListener);
    }

    public THSensor(String sid, OnHumidityChangeListener onHumidityChangeListener) {
        this(sid);
        setOnHumidityChangeListener(onHumidityChangeListener);
    }

    public THSensor(String sid, OnTemperatureChangeListener onTemperatureChangeListener, OnHumidityChangeListener onHumidityChangeListener) {
        this(sid);
        setOnTemperatureChangeListener(onTemperatureChangeListener);
        setOnHumidityChangeListener(onHumidityChangeListener);
    }

    public void setOnTemperatureChangeListener(OnTemperatureChangeListener onTemperatureChangeListener) {
        this.onTemperatureChangeListener = onTemperatureChangeListener;
    }

    public void setOnHumidityChangeListener(OnHumidityChangeListener onHumidityChangeListener) {
        this.onHumidityChangeListener = onHumidityChangeListener;
    }

    @Override
    public void parseData(String cmd) {
        try {
            JSONObject o = new JSONObject(cmd);

            if(!o.isNull(STATUS_TEMPERATURE)) {
                float currTemp = Float.parseFloat(o.getString(STATUS_TEMPERATURE)) / 100;

                if(temperature != null && Math.abs(currTemp - temperature) > 0.01) {
                    Optional.ofNullable(onTemperatureChangeListener).ifPresent(onTemperatureChangeListener -> onTemperatureChangeListener.onTemperatureChanged(currTemp));
                }

                temperature = currTemp;
            }

            if(!o.isNull(STATUS_HUMIDITY)) {
                float currHumidity = Float.parseFloat(o.getString(STATUS_HUMIDITY)) / 100;

                if(humidity != null && Math.abs(currHumidity - humidity) > 0.01) {
                    Optional.ofNullable(onHumidityChangeListener).ifPresent(onHumidityChangeListener -> onHumidityChangeListener.onHumidityChanged(currHumidity));
                }

                humidity = currHumidity;
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
    public String toString() {
        return super.toString() + "\ntemperature: " + temperature + "C, humidity: " + humidity + "%";
    }

    @Override
    public Bitmap getDevicePicture(Resources resources) {
        return BitmapFactory.decodeResource(resources, R.drawable.xiaomi_th_sensor);
    }

    public interface OnTemperatureChangeListener {

        void onTemperatureChanged(float t);
    }

    public interface OnHumidityChangeListener {

        void onHumidityChanged(float h);
    }
}
