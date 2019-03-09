package smarthome.library.datalibrary.store.listeners;

import smarthome.library.common.IotDevice;

public interface DeviceListener {
    void onDeviceReceived(IotDevice device);
}
