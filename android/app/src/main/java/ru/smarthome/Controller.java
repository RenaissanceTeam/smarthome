package ru.smarthome;

public class Controller {

    public long guid;
    public String type;
    public String state;

    @Override
    public String toString() {
        return "guid=" + guid + ", type=" + type +", state=" + state;
    }

}
