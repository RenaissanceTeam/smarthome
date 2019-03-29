package smarthome.library.common;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static smarthome.library.common.constants.DeviceTypes.DEFAULT_TYPE;
import static smarthome.library.common.constants.MessageConstantsKt.PENDING;

public class IotDevice {
    @Expose public String name;
    @Expose public String description;
    @Expose public long guid;
    @Expose public List<BaseController> controllers = new ArrayList<>();
    @Expose private String type = DEFAULT_TYPE;
    @Expose public String status = PENDING;

    public IotDevice() {} // needed for deserialization

    public IotDevice(String name, String description) {
        super();
        this.name = name;
        this.description = description;

        guid = GUID.getInstance().getGuidForIotDevice(this);
    }

    public void addControllers(BaseController... controllers) {
        this.controllers.addAll(Arrays.asList(controllers));
    }

    public void addControllers(List<BaseController> controllers) {
        this.controllers.addAll(controllers);
    }

    public List<BaseController> getControllers() {
        return controllers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Serialize to json using Gson, the class later can be deserialized using Gson
     */
    public String gsonned() {
        return new Gson().toJson(this);
    }

    @Override
    public int hashCode() {
        return (int)guid;
    }

    public boolean isIdentical(IotDevice device) {
        return equals(device)
                && Objects.equals(name, device.name)
                && Objects.equals(description, device.description)
                && Objects.equals(status, device.status);
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
