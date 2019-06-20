package smarthome.raspberry.service

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.raspberry.BuildConfig.DEBUG
import smarthome.raspberry.model.SmartHomeRepository
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.GatewayService
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Gateway
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.interfaces.AlarmHandler
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.interfaces.DeviceAddedListener

class GatewayServiceController : AlarmHandler, DeviceAddedListener {

    private val TAG = javaClass.name

    private val ioScope = CoroutineScope(Dispatchers.IO)

    private val gatewayServices: HashMap<String, GatewayService> = hashMapOf()

    fun bindGatewayService(password: String) {
        ioScope.launch {
            val service = GatewayService.builder()
                    .setGatewayPassword(password)
                    .setAddDeviceListener(this@GatewayServiceController)
                    .setAlarmHandler(this@GatewayServiceController)
                    .build()

            delay(5000)

            gatewayServices[service.gateway.sid] = service

            log(service)
        }
    }

    fun bindGatewayService(gateway: Gateway, gatewayDevices: MutableList<GatewayDevice>?) {
        ioScope.launch {
            val service = GatewayService.builder()
                    .setGateway(gateway)
                    .setDevices(gatewayDevices)
                    .setAddDeviceListener(this@GatewayServiceController)
                    .setAlarmHandler(this@GatewayServiceController)
                    .build()

            gatewayServices[gateway.sid] = service
            log(service)
        }
    }

    suspend fun removeGatewayService(gateway: Gateway) {
        val service = gatewayServices[gateway.sid]

        service?.removeDeviceAddedListener()
        service?.removeAlarmHandler(this)

        for (device in service!!.devices!!) {
            if (device == gateway)
                continue
            device.setRejected()
            SmartHomeRepository.delete(device)
        }

        service.kill()

    }

    override suspend fun onDeviceAdded(device: IotDevice) {
        SmartHomeRepository.addDevice(device)
    }

    override fun onAlarm(device: IotDevice, controller: BaseController) {
        SmartHomeRepository.handleAlert(device, controller)
    }

    private fun log(service: GatewayService) {
        if (DEBUG) Log.d(TAG, "service $service for gateway: ${service.gateway} binded \n" +
                "gatewayServices size: ${gatewayServices.size}")
    }

    companion object {
        private val instance = GatewayServiceController()

        fun getInstance(): GatewayServiceController {
            return instance
        }
    }
}