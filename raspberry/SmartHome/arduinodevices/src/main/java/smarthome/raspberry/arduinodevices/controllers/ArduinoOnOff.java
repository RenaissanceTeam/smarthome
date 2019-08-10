//package smarthome.raspberry.arduinodevices.controllers;
//
//import java.io.IOException;
//
//import smarthome.library.common.ControllerType;
//import smarthome.raspberry.arduinodevices.ArduinoControllerResponse;
//import smarthome.raspberry.arduinodevices.ArduinoDevice;
//
//public class ArduinoOnOff extends ArduinoController implements ArduinoWritable, ArduinoReadable {
//
//    public static final String TAG = ArduinoOnOff.class.getSimpleName();
//
//    public ArduinoOnOff(ArduinoDevice device, String name, ControllerType type,
//                        int indexInArduinoServicesArray) {
//        super(device, name, type, indexInArduinoServicesArray);
//    }
//
//    @Override
//    public ArduinoControllerResponse write(String value) throws IOException {
//        return baseWrite(value);
//    }
//
//    @Override
//    public ArduinoControllerResponse read() throws IOException {
//        return baseRead();
//    }
//}
