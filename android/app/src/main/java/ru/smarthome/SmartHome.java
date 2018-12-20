package ru.smarthome;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.smarthome.library.ArduinoDevice;

public class SmartHome {
    public static String baseUrl = "http://192.168.1.3:8080/";

    @SerializedName("devices")
    public List<ArduinoDevice> devices;

    @Override
    public String toString() {
        return "devices=" + devices.toString();
    }
}
