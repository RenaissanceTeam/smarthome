package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners;

import smarthome.library.common.BaseController;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice;

public interface SmokeAlarmListener {
    void onSmokeAlarmStatusChanged(boolean alarm, GatewayDevice device, BaseController controller);
}
