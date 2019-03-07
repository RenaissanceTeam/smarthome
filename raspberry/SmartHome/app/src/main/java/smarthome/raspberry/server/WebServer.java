package smarthome.raspberry.server;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;


import fi.iki.elonen.NanoHTTPD;
public class WebServer extends NanoHTTPD implements StoppableServer {

    public static final String TAG = WebServer.class.getSimpleName();
    public static final int PORT = 8080;

    public WebServer() {
        super(PORT);
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            return HandlerType.handle(session);
        } catch (Exception e) {
            Log.d(TAG, "can't serve: " + e);
            return HandlerType.errorHandle("No suitable method found");
        }
    }

    @Override
    public void startServer() {
        try {
            start();
        } catch (IOException e) {
            Log.e(TAG, "startServer: ", e);
        }
    }

    @Override
    public void stopServer() {
        stop();
    }
}
