//package smarthome.raspberry.arduinodevices.controllers;
//
//import java.io.IOException;
//
//import retrofit2.Call;
//import smarthome.library.common.ControllerType;
//import smarthome.raspberry.arduinodevices.data.dto.ArduinoControllerResponse;
//import smarthome.raspberry.arduinodevices.domain.ArduinoDevice;
//import smarthome.raspberry.arduinodevices.ArduinoDeviceAPI;
//
//public class ArduinoReadFloat extends ArduinoController implements ArduinoReadable {
//
//    public ArduinoReadFloat(ArduinoDevice device, String name, ControllerType type,
//                            int indexInArduinoServicesArray) {
//        super(device, name, type, indexInArduinoServicesArray);
//    }
//
//    @Override
//    public ArduinoControllerResponse read() throws IOException {
//        ArduinoDeviceAPI arduinoApi = getArduinoDeviceAPI();
//        Call<ArduinoControllerResponse> call = arduinoApi.controllerReadRequest(getIndexInArduinoServicesArray());
//
//        ArduinoControllerResponse controllerResponse = call.execute().body();
//        if (controllerResponse != null) {
//            String response = controllerResponse.response;
//            double val = Double.parseDouble(response);
//            controllerResponse.response = ((int)(val * 100 / 1025)) + " %";
//            setNewState(controllerResponse.response);
//        }
//        return controllerResponse;
//    }
//}
