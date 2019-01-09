package ru.smarthome.library;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class IotDevice {
    @Expose public String name;
    @Expose public String description;
    @Expose public long guid;
    @Expose public List<BaseController> controllers; // todo incapsulation (everywhere)

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
