package smarthome.raspberry.input.datasource

import io.reactivex.Observable
import smarthome.library.common.DeviceUpdate
import smarthome.library.common.SmartHomeStorage
import smarthome.raspberry.data.HomeInfoSource
import smarthome.raspberry.input.InputControllerDataSource

class InputFromSharedDatabase(private val homeInfoSource: HomeInfoSource,
                              private val databaseFactory: (String) -> SmartHomeStorage) :
        InputControllerDataSource {
    private var storage: SmartHomeStorage? = null
    private suspend fun getStorage(): SmartHomeStorage {
        if (storage == null) {
            storage = databaseFactory(homeInfoSource.getUserId())
        }
        return storage!!
    }

    override suspend fun getDeviceUpdates(): Observable<DeviceUpdate> {
        return getStorage().observeDevicesUpdates()
    }
}