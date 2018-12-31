package ru.smarthome.library;

// TODO: 12/20/18 actually it'd be better to put this in library, so we have two identical classes
// in both client and server code
public class Controller {

    public long guid;
    public String type;
    public String state;

    @Override
    public String toString() {
        return "guid=" + guid + ", type=" + type +", state=" + state;
    }

}
