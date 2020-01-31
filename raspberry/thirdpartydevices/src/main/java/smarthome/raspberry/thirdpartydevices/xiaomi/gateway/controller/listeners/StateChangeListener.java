package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners;

import smarthome.library.common.Controller;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice;

public interface StateChangeListener {
    void onStateChanged(String state, GatewayDevice device, Controller controller);
}
