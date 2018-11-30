package raspberry.smarthome.model.device;

import java.util.List;
import java.util.Random;

import raspberry.smarthome.model.Entity;
import raspberry.smarthome.model.device.controllers.BaseController;

public abstract class IotDevice extends Entity {
    List<BaseController> controllers;

    public abstract boolean connect();

    public abstract boolean disconnect();

    public IotDevice(String... params) {
        super();
        GUID = generateGUID(params);
    }

    @Override
    protected long generateGUID(String... params) {
        return params.length == 0 ? new Random().nextLong() : params[0].hashCode();
    }

    public List<BaseController> getControllers() {
        return controllers;
    }

    @Override
    public int hashCode() {
        return (int)GUID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IotDevice) {
            return ((IotDevice) obj).GUID == GUID;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "IotDevice{" +
                "controllers=" + controllers +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", GUID=" + GUID +
                '}';
    }
}
