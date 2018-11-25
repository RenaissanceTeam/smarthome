package ru.smarthome.network.calls;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiResponse<T> implements Callback<T> {

    private OnApiResponse<T> listener;

    public ApiResponse(OnApiResponse<T> listener, Class<T> cl) {
        this.listener = listener;
    }


    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (listener != null)
            listener.onResponseReceived(response.body());

    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        if (listener != null)
            listener.onError();
    }

}
