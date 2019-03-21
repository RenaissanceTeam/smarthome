package smarthome.raspberry.model

import android.content.Context
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.storage.JsonDataSource
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.controllers.*
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.*
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.RGBController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.*

enum class KDataSources (val deviceType: Class<out IotDevice>,
                         vararg controllerTypes: Class<out BaseController>) {

    ARDUINO(ArduinoDevice::class.java,
            ArduinoDigitalAlert::class.java, ArduinoHumidity::class.java, ArduinoInit::class.java, ArduinoOnOff::class.java,
            ArduinoReadFloat::class.java, ArduinoTemperature::class.java),

    GATEWAY_DEVICE(GatewayDevice::class.java,
            ButtonClickController::class.java, ButtonController::class.java, DoorWindowController::class.java,
            GatewayLightOnOffController::class.java, HumidityController::class.java, IlluminationController::class.java,
            LoadPowerController::class.java, MotionController::class.java, PowerConsumedController::class.java,
            PressureController::class.java, RGBController::class.java, SmartPlugOnOffController::class.java,
            SmokeAlarmController::class.java, SmokeDensityController::class.java, TemperatureController::class.java,
            TimeInUseController::class.java, VoltageController::class.java, WaterLeakController::class.java),

    YEELIGHT_DEVICE(YeelightDevice::class.java,
            AdjustController::class.java, BrightnessController::class.java, ColorTemperatureController::class.java,
            CronController::class.java, DefaultController::class.java, DeleteCronController::class.java,
            FlowController::class.java, HSVController::class.java, NameController::class.java,
            PowerController::class.java, ToggleController::class.java,
            smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.RGBController::class.java);

    lateinit var source: JsonDataSource
    var controllerType: MutableList<Class<out BaseController>> = controllerTypes.toMutableList()

    internal fun init(context: Context) {
        source = JsonDataSource(context.applicationContext, deviceType, controllerType)
    }
}