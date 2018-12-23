package ru.smarthome.library;

import java.util.List;

import ru.smarthome.library.Controller;

// TODO: 12/20/18 actually it'd be better to put this in library, so we have two identical classes
// in both client and server code
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
