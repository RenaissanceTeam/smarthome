package ru.smarthome.library;

import com.google.gson.annotations.Expose;

/**
 * Basic controller interface
 * <br>All controllers must implement this interface
 */
public class BaseController {
    @Expose public ControllerType type;
    @Expose public long guid;
    @Expose public String state;

    public void setNewState(String newState) {}// todo is "String" enough for state?
}
