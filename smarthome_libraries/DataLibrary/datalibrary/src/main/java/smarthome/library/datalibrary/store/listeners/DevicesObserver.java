package smarthome.library.datalibrary.store.listeners;

import smarthome.library.common.IotDevice;

import java.util.List;

public interface DevicesObserver {
    /**
     * @param devices devices whose data has been updated
     * @param isInnerCall indicates that data changed on subscriber (if true)
     */
    void onDevicesChanged(List<IotDevice> devices, boolean isInnerCall);
}