package smarthome.library.datalibrary.store.listeners

import smarthome.library.common.IotDevice

interface DevicesObserver {
    /**
     * @param devices devices whose data has been updated
     * @param isInnerCall indicates that data changed on subscriber (if true)
     */
    fun onDevicesChanged(devices: List<IotDevice>, isInnerCall: Boolean)
}