package raspberry.smarthome;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD {

    public static final String TAG = WebServer.class.getSimpleName();

    public WebServer() {
        super(8080);
    }

    @Override
    public Response serve(String uri, Method method,
                          Map<String, String> header,
                          Map<String, String> parameters,
                          Map<String, String> files) {
        String answer = "hi";

        return new NanoHTTPD.Response(answer);
    }
}
