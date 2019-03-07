package smarthome.raspberry.server.httphandlers;

import android.util.Log;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import smarthome.library.common.BaseController;
import smarthome.raspberry.arduinodevices.ArduinoDevice;
import smarthome.raspberry.model.RaspberrySmartHome;

public class AlertPost extends BaseRequestHandler {

    public static final String TAG = AlertPost.class.getSimpleName();

    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        try {
            serveAlertRequest(session);
            return new NanoHTTPD.Response("alert ok");
        } catch (Exception e) {
            return new NanoHTTPD.Response("alert exception " + e);
        }
    }

    private void serveAlertRequest(NanoHTTPD.IHTTPSession session) throws NumberFormatException{
        Map<String, String> params = session.getParms();
        int index = Integer.parseInt(params.get("ind"));
        String value = params.get("value");
        String ip = session.getHeaders().get("http-client-ip");

        ArduinoDevice arduino = RaspberrySmartHome.getInstance().getArduinoByIp(ip);
        BaseController controller = arduino.controllers.get(index);
        controller.state = value;
        Log.d(TAG, "alert! new state=" + value + "for controller " + controller);
        // todo save to firestore (notify android client, send FCM)
    }

}
