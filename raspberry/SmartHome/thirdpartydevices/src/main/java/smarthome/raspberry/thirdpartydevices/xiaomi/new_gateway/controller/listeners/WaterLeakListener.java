package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.listeners;

public interface WaterLeakListener {
    void onWaterLeakStatusChanged(boolean leak);
}
