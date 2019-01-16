package raspberry.smarthome.thirdpartydevices.xiaomi.yeelight;

import android.content.res.Resources;
import android.graphics.Bitmap;

import raspberry.smarthome.thirdpartydevices.Device;

public class YeelightDevice extends Device {

    private int id;

    private String ip;

    private String port;

    @Override
    public Bitmap getDevicePicture(Resources resources) {
        return null; //TODO: implement
    }
}
