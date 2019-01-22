package ru.smarthome.arduinodevices.controllers;

import java.io.IOException;

import ru.smarthome.arduinodevices.ArduinoControllerResponse;

public interface ArduinoReadable {
    /**
     * Synchronous http request, returns json
     */
    ArduinoControllerResponse read() throws IOException;
}
