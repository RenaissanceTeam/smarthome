package smarthome.raspberry.data

import io.reactivex.Observable
import smarthome.library.common.BaseController
import smarthome.library.common.DeviceUpdate

interface RemoteStorage {
    suspend fun init()
    suspend fun onControllerChanged(controller: BaseController)
    suspend fun createHome(homeId: String)
    suspend fun isHomeIdUnique(homeId: String): Boolean
    suspend fun getDevices(): Observable<DeviceUpdate>
}