package ru.smarthome.network.calls;

public interface OnApiResponse<T> {

    void onResponseReceived(T response);

    void onError();

}
