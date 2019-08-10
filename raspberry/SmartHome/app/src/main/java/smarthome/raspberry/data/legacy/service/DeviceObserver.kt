//package smarthome.raspberry.data.legacy.service
//
//import android.util.Log
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import smarthome.raspberry.BuildConfig.DEBUG
//import smarthome.raspberry.data.legacy.model.SmartHomeRepository
//import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.discover.DeviceDetector
//
//class DeviceObserver private constructor() {
//    private val TAG = javaClass.name
//
//    private val yeelightDeviceDetector: DeviceDetector = DeviceDetector()
//
//    private val scope = CoroutineScope(Dispatchers.Default)
//
//
//    // entry for non setup needed devices
//    fun start() {
//        if (DEBUG) Log.d(TAG, "searching for new devices")
//        scope.launch {
//            val devices = yeelightDeviceDetector.discover()
//            for (device in devices)
//                SmartHomeRepository.addDevice(device)
//        }
//    }
//
//    fun exploreGateway(password: String) {
//        if (DEBUG) Log.d(TAG, "setting up new xiaomi gateway")
//        GatewayServiceController.getInstance().bindGatewayService(password)
//    }
//
//    companion object {
//        private val instance = DeviceObserver()
//
//        fun getInstance() : DeviceObserver {
//            return instance
//        }
//    }
//}