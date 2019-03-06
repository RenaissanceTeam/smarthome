package smarthome.raspberry.arduinodevices.controllers;

import java.io.IOException;

import smarthome.raspberry.arduinodevices.ArduinoControllerResponse;

public interface ArduinoReadable {
    /**
     * Synchronous http request, returns json
     */
    ArduinoControllerResponse read() throws IOException;
}
