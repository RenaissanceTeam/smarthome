package raspberry.smarthome.model.device.listeners;

public interface DimmerListener extends BaseListener {

    void onStateChanged(int state);

}