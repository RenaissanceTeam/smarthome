package smarthome.client.domain.api.usecase

import io.reactivex.Observable
import smarthome.client.entity.Device

interface DevicesUseCase {
    suspend fun getDevices(): Observable<MutableList<Device>>
    suspend fun changeDevice(device: Device)
    
    suspend fun getDevice(deviceGuid: Long): Device?
}
