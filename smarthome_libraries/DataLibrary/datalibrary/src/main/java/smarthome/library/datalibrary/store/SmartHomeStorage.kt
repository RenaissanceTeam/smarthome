package smarthome.library.datalibrary.store

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import smarthome.library.datalibrary.constants.defFailureListener
import smarthome.library.datalibrary.constants.defSuccessListener
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.SmartHome
import smarthome.library.datalibrary.store.listeners.SmartHomeListener

interface SmartHomeStorage {

    fun postSmartHome(
        smartHome: SmartHome,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    /**
     * IotDevice update function <br>
     * If controller is not null and belongs to current IotDevice, then controllers serveState changes to pending
     * @param device device which need to be updated
     * @param controller BaseController which serveState should be pending
     * @param successListener OnSuccessListener, default implementation perform logging
     * @param failureListener OnFailureListener, default implementation perform logging
     */
    fun updateSmartHomeDevice(
        device: IotDevice, controller: BaseController? = null,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun getSmartHome(listener: SmartHomeListener)
}