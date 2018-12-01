package raspberry.smarthome.model.device.controllers;

public interface Writable<T> {
    void write(T value);
}
