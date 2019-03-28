package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners;

public interface WaterLeakListener {
    void onWaterLeakStatusChanged(boolean leak);
}
