package raspberry.smarthome.model.device.controllers;

import raspberry.smarthome.model.device.ArduinoIotDevice;
import ru.smarthome.library.ControllerType;

public class ControllersFactory {

    public static ArduinoController createArduinoController(ControllerType type,
                                                            ArduinoIotDevice device,
                                                            int index) {
        switch (type) {
            case HUMIDITY: return new ArduinoHumidity(device, type, index);
            case TEMPERATURE: return new ArduinoTemperature(device, type, index);
            case ARDUINO_ANALOG: return new ArduinoReadFloat(device, type, index);
            case ARDUINO_ON_OFF: return new ArduinoOnOff(device, type, index);
        }

        return null;
    }
}
