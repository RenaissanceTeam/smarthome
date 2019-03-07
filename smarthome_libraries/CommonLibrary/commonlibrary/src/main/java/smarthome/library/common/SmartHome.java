package smarthome.library.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SmartHome {

    @SerializedName("devices") @Expose
    public List<IotDevice> devices;
}
