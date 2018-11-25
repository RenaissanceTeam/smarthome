package ru.smarthome.network.model;

public class User extends Entity {

    public String email;

    public RaspberryInfo raspberryInfo;

    public User () {}

    public User(String email, RaspberryInfo raspberryInfo) {
        this.email = email;
        this.raspberryInfo = raspberryInfo;
    }

    public User (User other) {
        this(other.email, other.raspberryInfo);
    }
}
