package raspberry.smarthome.model.device.discovering;

import java.util.List;

import raspberry.smarthome.model.device.IotDevice;

public interface DeviceDiscoveringCallback {

    void onDevicesDiscovered(List<IotDevice> devices);

}
