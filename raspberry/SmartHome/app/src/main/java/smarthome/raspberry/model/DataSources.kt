package smarthome.raspberry.model

import android.content.Context
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.storage.JsonDataSource
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.controllers.*
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.*
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.RGBController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.*
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.*

enum class DataSources (val deviceType: Class<out IotDevice>,
                        vararg controllerTypes: Class<out BaseController>) {

    ARDUINO(ArduinoDevice::class.java,
            ArduinoDigitalAlert::class.java, ArduinoHumidity::class.java, ArduinoInit::class.java, ArduinoOnOff::class.java,
            ArduinoReadFloat::class.java, ArduinoTemperature::class.java),

    GATEWAY(Gateway::class.java,
            RGBController::class.java, IlluminationController::class.java, GatewayLightOnOffController::class.java),

    GATEWAY_DOOR_WINDOW_SENSOR(DoorWindowSensor::class.java,
            DoorWindowController::class.java, VoltageController::class.java),

    GATEWAY_MOTION_SENSOR(MotionSensor::class.java,
            MotionController::class.java, VoltageController::class.java),

    GATEWAY_SMART_PLUG(SmartPlug::class.java,
            SmartPlugOnOffController::class.java, TimeInUseController::class.java, PowerConsumedController::class.java,
            PowerController::class.java, LoadPowerController::class.java, VoltageController::class.java),

    GATEWAY_SMOKE_SENSOR(SmokeSensor::class.java,
            SmokeAlarmController::class.java, SmokeDensityController::class.java, VoltageController::class.java),

    GATEWAY_T_H_SENSOR(THSensor::class.java,
            TemperatureController::class.java, HumidityController::class.java, VoltageController::class.java),

    GATEWAY_WATER_LEAK_SENSOR(WaterLeakSensor::class.java,
            WaterLeakController::class.java, VoltageController::class.java),

    GATEWAY_WEATHER_SENSOR(WeatherSensor::class.java,
            TemperatureController::class.java, HumidityController::class.java,
            PressureController::class.java, VoltageController::class.java),

    GATEWAY_WIRED_DUAL_WALL_SWITCH(WiredDualWallSwitch::class.java,
            ButtonController::class.java),

    GATEWAY_WIRED_SINGLE_WALL_SWITCH(WiredSingleWallSwitch::class.java,
            ButtonController::class.java),

    GATEWAY_WIRELESS_SWITCH(WirelessSwitch::class.java,
            ButtonClickController::class.java),

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