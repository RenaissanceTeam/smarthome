package raspberry.smarthome.model.device;

import java.util.ArrayList;
import java.util.List;

import raspberry.smarthome.model.GUID;
import raspberry.smarthome.model.device.controllers.BaseController;

public abstract class IotDevice {
    public String name;
    public String description;
    public final long guid;
    public List<BaseController> controllers; // todo incapsulation (everywhere)

    public IotDevice(String name, String description) {
        super();
        this.name = name;
        this.description = description;
        this.controllers = new ArrayList<>();

        guid = GUID.getInstance().getGuidForIotDevice(this);
    }

    public List<BaseController> getControllers() {
        return controllers;
    }

    @Override
    public int hashCode() {
        return (int)guid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IotDevice) {
            return ((IotDevice) obj).guid == guid;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "IotDevice{" +
                "controllers=" + controllers +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", GUID=" + guid +
                '}';
    }
}
