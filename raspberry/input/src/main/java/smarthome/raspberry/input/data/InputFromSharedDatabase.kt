package smarthome.raspberry.input.data

import io.reactivex.disposables.Disposable
import kotlinx.coroutines.runBlocking
import smarthome.library.common.DeviceUpdate
import smarthome.library.datalibrary.api.SmartHomeStorage
import smarthome.raspberry.authentication.api.domain.GetUserIdUseCase

class InputFromSharedDatabase(private val getUserIdUseCase: GetUserIdUseCase,
                              private val databaseFactory: (String) -> smarthome.library.datalibrary.api.SmartHomeStorage) :
        InputControllerDataSource {
    private var storage: smarthome.library.datalibrary.api.SmartHomeStorage? = null

    private var uidSubscription: Disposable? = null
    private var deviceUpdateSubscription: Disposable? = null

    override fun setActionForNewDeviceUpdate(action: (DeviceUpdate) -> Unit) {
        uidSubscription?.dispose()
        uidSubscription = getUserIdUseCase.execute().subscribe {
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