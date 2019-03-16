package smarthome.library.common;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class IotDevice {
    @Expose public String name;
    @Expose public String description;
    @Expose public long guid;
    @Expose public List<BaseController> controllers;

    public IotDevice() {} // needed for deserialization

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
