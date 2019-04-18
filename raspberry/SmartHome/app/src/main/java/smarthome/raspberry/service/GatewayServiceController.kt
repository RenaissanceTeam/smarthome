package smarthome.raspberry.service

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import smarthome.raspberry.BuildConfig.DEBUG
import smarthome.raspberry.model.SmartHomeRepository
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.GatewayService
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Gateway
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice

class GatewayServiceController {

    private val TAG = javaClass.name

    private val ioScope = CoroutineScope(Dispatchers.IO)

    private val gatewayServices: HashMap<String, GatewayService> = hashMapOf()

    fun bindGatewayService(password: String) {
        ioScope.launch {
            val service = GatewayService.builder()
                    .setGatewayPassword(password)
                    .build()

            delay(20000)

            gatewayServices[service.gateway.sid] = service

            if (service.gateway != null) {
                for (device in service.devices)
                    SmartHomeRepository.addDevice(device)
            }
            log(service)
        }
    }

    fun bindGatewayService(gateway: Gateway, gatewayDevices: MutableList<GatewayDevice>?) {
        ioScope.launch {
            val service = GatewayService.builder()
                    .setGateway(gateway)
                    .setDevices(gatewayDevices)
                    .build()

            delay(5000)

            gatewayServices[gateway.sid] = service
            log(service)
        }
    }

    suspend fun removeGatewayService(gateway: Gateway) {
        val service = gatewayServices[gateway.sid]

        for (device in service!!.devices!!) {
            if (device == gateway)
                continue
            device.setRejected()
            SmartHomeRepository.delete(device)
        }

        service.kill()

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