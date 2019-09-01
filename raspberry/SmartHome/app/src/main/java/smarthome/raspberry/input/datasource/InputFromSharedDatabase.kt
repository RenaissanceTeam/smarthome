package smarthome.raspberry.input.datasource

import io.reactivex.disposables.Disposable
import kotlinx.coroutines.runBlocking
import smarthome.library.common.DeviceUpdate
import smarthome.library.common.SmartHomeStorage
import smarthome.raspberry.data.HomeInfoSource
import smarthome.raspberry.input.InputControllerDataSource

class InputFromSharedDatabase(private val homeInfoSource: HomeInfoSource,
                              private val databaseFactory: (String) -> SmartHomeStorage) :
        InputControllerDataSource {
    private var storage: SmartHomeStorage? = null

    private var uidSubscription: Disposable? = null
    private var deviceUpdateSubscription: Disposable? = null

    override fun setActionForNewDeviceUpdate(action: (DeviceUpdate) -> Unit) {
        uidSubscription?.dispose()
        uidSubscription = homeInfoSource.getObservableUserId().subscribe {
            deviceUpdateSubscription?.dispose()
            val database = databaseFactory(it)
            storage = database

            runBlocking {
                deviceUpdateSubscription =
                        database.observeDevicesUpdates()
                                .mergeWith(database.observePendingDevicesUpdates())
                                .subscribe(action)
            }
        }
    }
}