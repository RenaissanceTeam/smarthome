package smarthome.raspberry.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import smarthome.raspberry.model.SmartHomeRepository
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.GatewayService
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.discover.DeviceDetector

class DeviceObserver private constructor() {
    // parentGatewaySid, GatewayService
    val gatewayServices: HashMap<String, GatewayService> = HashMap() // TODO: save state and recreate gatewayServices on app start
    val yeelightDeviceDetector: DeviceDetector = DeviceDetector()

    private val scope = CoroutineScope(Dispatchers.Default)


    // entry for non setup needed devices
    fun start() {
        scope.launch {
            val devices = yeelightDeviceDetector.discover()
            for (device in devices)
                SmartHomeRepository.addDevice(device)
        }
    }

    fun exploreGateway(password: String) {
        scope.launch {
            val service = GatewayService.builder()
                    .setGatewayPassword(password)
                    .build()
            delay(20000)
            if (service.gateway != null) {
                gatewayServices[service.gateway.sid] = service
                for (device in service.devices)
                    SmartHomeRepository.addDevice(device)
            }
        }
    }

    companion object {
        private val instance = DeviceObserver()

        fun getInstance() : DeviceObserver {
            return instance
        }
    }
}