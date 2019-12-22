//package smarthome.raspberry.arduinodevices.controllers;
//
//import java.io.IOException;
//
//import smarthome.raspberry.arduinodevices.data.dto.ArduinoControllerResponse;
//
//public interface ArduinoWritable {
//    /**
//     * NOTE !! Synchronous !! http post request to device's web server
//     * @param value {0, 1} for digital arduino pin, [0..1] for analog arduino pin, etc...
//     * @return return body of response from device's web server
//     * @throws IOException
//     */
//    ArduinoControllerResponse write(String value) throws IOException;
//}