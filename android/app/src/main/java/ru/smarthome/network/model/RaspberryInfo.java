package ru.smarthome.network.model;

public class RaspberryInfo extends Entity {

    public String ip;

    public String port;

    public String getUrl() {
        return ip + ":" + port;
    }

}
