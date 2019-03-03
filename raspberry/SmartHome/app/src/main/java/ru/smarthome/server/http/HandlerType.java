package ru.smarthome.server.http;

import fi.iki.elonen.NanoHTTPD;
import ru.smarthome.server.http.handlers.ControllerGet;
import ru.smarthome.server.http.handlers.RequestHandler;

import static fi.iki.elonen.NanoHTTPD.Method.GET;

public enum HandlerType {
    CONTROLLER(GET, new ControllerGet());

    private RequestHandler handler;
    HandlerType(NanoHTTPD.Method method, RequestHandler handler) {
        this.handler = handler;
    }
}
