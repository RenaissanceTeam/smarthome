package smarthome.library.datalibrary.store.listeners;

import smarthome.library.common.IotDevice;

import java.util.List;

public interface PendingDevicesFetchListener {
    void onPendingDevicesFetched(List<IotDevice> pendingDevices);
}
