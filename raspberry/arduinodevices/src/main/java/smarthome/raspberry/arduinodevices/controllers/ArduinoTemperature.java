//package smarthome.raspberry.arduinodevices.controllers;
//
//import java.io.IOException;
//
//import smarthome.library.common.ControllerType;
//import smarthome.raspberry.arduinodevices.ArduinoControllerResponse;
//import smarthome.raspberry.arduinodevices.ArduinoDevice;
//
//public class ArduinoTemperature extends ArduinoController implements ArduinoReadable {
//
//    public ArduinoTemperature(ArduinoDevice device, String name, ControllerType type,
//                              int indexInArduinoServicesArray) {
//        super(device, name, type, indexInArduinoServicesArray);
//    }
//
//    @Override
//    public ArduinoControllerResponse read() throws IOException {
//        return baseRead();
//    }
//}