package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces;

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result;

public interface WriteCommandListener {
    void onWriteCompleted(Result result);
}
