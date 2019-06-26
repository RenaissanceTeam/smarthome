package smarthome.client.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import smarthome.client.R
import smarthome.library.common.IotDevice
import smarthome.library.common.constants.DeviceTypes.*

object DeviceDescriptor {

    fun getDevicePicture(res: Resources, device: IotDevice): Bitmap {
        return getDevicePicture(res, device.type)
    }
    
    private fun getDevicePicture(res: Resources, deviceType: String): Bitmap {
        val deviceResource = typesMap[deviceType] ?: throw IllegalArgumentException("no resource for provided device type found")

        return BitmapFactory.decodeResource(res, deviceResource)
    }

    private val typesMap: HashMap<String, Int> = hashMapOf(
            GATEWAY_TYPE                     to R.drawable.xiaomi_gateway_2,
            DOOR_WINDOW_SENSOR_TYPE          to R.drawable.xiaomi_door_window_sensor,
            MOTION_SENSOR_TYPE               to R.drawable.xiaomi_motion_sensor,
            WIRELESS_SWITCH_TYPE             to R.drawable.xiaomi_wireless_switch,
            TEMPERATURE_HUMIDITY_SENSOR_TYPE to R.drawable.xiaomi_th_sensor,
            WATER_LEAK_SENSOR_TYPE           to R.drawable.xiaomi_water_leak_sensor,
            WEATHER_SENSOR_TYPE              to R.drawable.xiaomi_thp_sensor,
            WIRED_DUAL_WALL_SWITCH_TYPE      to R.drawable.xiaomi_wired_dual_wall_switch,
            WIRED_SINGLE_WALL_SWITCH_TYPE    to R.drawable.xiaomi_wired_single_wall_switch,
            SMART_PLUG_TYPE                  to R.drawable.xiaomi_smart_plug,
            SMOKE_SENSOR_TYPE                to R.drawable.xiaomi_smoke_sensor
    )
}