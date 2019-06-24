//package smarthome.raspberry.arduinodevices.controllers;
//
//import smarthome.library.common.ControllerType;
//import smarthome.raspberry.arduinodevices.ArduinoDevice;
//
//public class ArduinoControllersFactory {
//
//    public static ArduinoController createArduinoController(ControllerType type,
//                                                            String name,
//                                                            ArduinoDevice device,
//                                                            int index) {
//        switch (type) {
//            case HUMIDITY_DHT11:
//            case HUMIDITY_DHT22:
//                return new ArduinoHumidity(device, name, type, index);
//            case TEMPERATURE_DHT11:
//            case TEMPERATURE_DHT22:
//                return new ArduinoTemperature(device, name, type, index);
//            case ARDUINO_ANALOG:
//                return new ArduinoReadFloat(device, name, type, index);
//            case ARDUINO_ON_OFF:
//                return new ArduinoOnOff(device, name, type, index);
//            case DIGITAL_ALERT:
//                return new ArduinoDigitalAlert(device, name, type, index);
//            case INIT:
//                return new ArduinoInit(device, name, type, index);
//            // TODO: 3/8/19 add pressure
//        }
//
//        return null;
//    }
//}
