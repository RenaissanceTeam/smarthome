package ru.smarthome;

import java.util.List;

public class ArduinoDevice {

    public String name;
    public String description;
    public long guid;
    public List<Controller> controllers;

    @Override
    public String toString() {
        return "name=" + name +", controllers=" + controllers;
    }
}
