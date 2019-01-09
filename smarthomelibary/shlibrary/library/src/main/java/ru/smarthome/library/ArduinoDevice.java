package ru.smarthome.library;

import java.util.List;

public class ArduinoDevice {

    public String name;
    public String description;
    public long guid;
    public List<BaseController> controllers;

    @Override
    public String toString() {
        return "name=" + name +", controllers=" + controllers;
    }
}
