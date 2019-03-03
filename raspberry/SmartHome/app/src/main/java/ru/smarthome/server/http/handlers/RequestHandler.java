package ru.smarthome.server.http.handlers;

import fi.iki.elonen.NanoHTTPD;

public interface RequestHandler {
    NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session);
}
