package ru.smarthome;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.smarthome.library.ArduinoDevice;
import ru.smarthome.library.Controller;
import ru.smarthome.library.RaspberryResponse;

import static ru.smarthome.MainActivity.getRaspberryApi;

public class ControllerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public static final String TAG = ControllerViewHolder.class.getSimpleName();
    public static final String UNKNOWN_STATE = "-";
    private TextView guid;
    private TextView type;
    private TextView state;
    private ProgressBar progressBar;

    private ArduinoDevice device;
    private Controller controller;

    public ControllerViewHolder(View itemView) {
        super(itemView);

        guid = itemView.findViewById(R.id.controller_guid);
        type = itemView.findViewById(R.id.type);
        state = itemView.findViewById(R.id.state);
        progressBar = itemView.findViewById(R.id.progress);

        itemView.setOnClickListener(this);
    }

    public void bind(ArduinoDevice device, Controller controller) {
        if (controller == null || device == null) {
            return;
        }
        this.controller = controller;
        this.device = device;

        guid.setText(controller.guid + "");
        type.setText(controller.type);
        if (controller.state != null) {
            state.setText(controller.state);
        } else {
            state.setText(UNKNOWN_STATE);
        }
    }

    @Override
    public void onClick(View v) {
        if (controller.type.equals("ARDUINO_ON_OFF")) {
            if (UNKNOWN_STATE.equals(state.getText())) {
                readState();
                return;
            }
            if ("1".equals(controller.state)) {
                changeStateTo("0");
            } else {
                changeStateTo("1");
            }

        } else {
            readState();
        }
    }

    private void startStateChange() {
        progressBar.setVisibility(View.VISIBLE);
        state.setVisibility(View.GONE);
    }

    private void endStateChange() {
        progressBar.setVisibility(View.GONE);
        state.setVisibility(View.VISIBLE);
    }

    private void readState() {
        startStateChange();
        RaspberryApi api = getRaspberryApi();
        Call<RaspberryResponse> call = api.readControllerState(device.guid, controller.guid);
        call.enqueue(new Callback<RaspberryResponse>() {
            @Override
            public void onResponse(Call<RaspberryResponse> call, Response<RaspberryResponse> response) {
                handleResponseWithState(response);
            }

            @Override
            public void onFailure(Call<RaspberryResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
                endStateChange();
            }
        });

    }

    private void changeStateTo(String value) {
        startStateChange();
        RaspberryApi api = getRaspberryApi();
        Call<RaspberryResponse> call = api.changeControllerState(device.guid, controller.guid, value);
        call.enqueue(new Callback<RaspberryResponse>() {
            @Override
            public void onResponse(Call<RaspberryResponse> call, Response<RaspberryResponse> response) {
                handleResponseWithState(response);
            }

            @Override
            public void onFailure(Call<RaspberryResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
                endStateChange();
            }
        });

    }

    private void handleResponseWithState(Response<RaspberryResponse> response) {
        if (response.isSuccessful()) {

            RaspberryResponse raspberryResponse = response.body();
            if (raspberryResponse == null) {
                endStateChange();
                return;
            }
            String newState = raspberryResponse.response;
            state.setText(newState);
            controller.state = newState;
        } else {
            String message = "Returned code: " + response.code();
            try {
                message += ", " + response.raw().body().string();
            } catch (Exception ignored) {}
            
            Toast.makeText(guid.getContext(), message, Toast.LENGTH_LONG).show();
        }
        endStateChange();
    }
}

