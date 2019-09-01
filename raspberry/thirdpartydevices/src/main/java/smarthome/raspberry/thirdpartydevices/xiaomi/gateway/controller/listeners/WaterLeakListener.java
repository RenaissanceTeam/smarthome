package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners;

import smarthome.library.common.BaseController;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice;

public interface WaterLeakListener {
    void onWaterLeakStatusChanged(boolean leak, GatewayDevice device, BaseController controller);
}
