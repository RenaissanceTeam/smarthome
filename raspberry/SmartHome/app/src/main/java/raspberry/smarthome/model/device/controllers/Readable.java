package raspberry.smarthome.model.device.controllers;

// TODO: 11/30/2018 actually won't return value immediately, instead return with request
public interface Readable<T> {
    public T read();
}
