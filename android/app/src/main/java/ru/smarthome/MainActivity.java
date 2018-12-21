package ru.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.smarthome.library.ArduinoDevice;
import ru.smarthome.library.Controller;
import ru.smarthome.library.RaspberryResponse;

import static ru.smarthome.constants.Constants.RC_SIGN_IN;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView controllers;
    private SmartHomeControllersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout = findViewById(R.id.refresh_controllers);
        controllers = findViewById(R.id.controllers);
        controllers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SmartHomeControllersAdapter();
        controllers.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestInfoFromRaspberry();
            }
        });
//        auth();
        requestInfoFromRaspberry(); // todo after auth successful
    }

    private void stopRefreshing() {
        refreshLayout.setRefreshing(false);
    }

    private void auth() {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            } else {
                // TODO: guide user to the xuy
            }
        }
    }

    private void requestInfoFromRaspberry() {
        RaspberryApi api = getRaspberryApi();
        api.getSmartHomeState().enqueue(new Callback<SmartHome>() {
            @Override
            public void onResponse(Call<SmartHome> call, Response<SmartHome> response) {
                Log.d(TAG, "onResponse: " + response.body());
                adapter.setSmartHome(response.body());
                stopRefreshing();
            }

            @Override
            public void onFailure(Call<SmartHome> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
                stopRefreshing();
            }
        });
    }

    @NonNull
    private static RaspberryApi getRaspberryApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SmartHome.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(RaspberryApi.class);
    }

    private class SmartHomeControllersAdapter extends RecyclerView.Adapter<ControllerViewHolder> {
        private SmartHome smartHome;

        public void setSmartHome(SmartHome smartHome) {
            this.smartHome = smartHome;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ControllerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.controller_item, parent, false);
            return new ControllerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ControllerViewHolder holder, int position) {
            ArduinoDevice device = getDeviceByControllerPosition(position);
            Controller controller = getControllerByPosition(position);
            holder.bind(device, controller);
        }

        @Override
        public int getItemCount() {
            if (smartHome == null || smartHome.devices == null) {
                return 0;
            }
            return countAllControllers();
        }

        private int countAllControllers() {
            int res = 0;
            for (ArduinoDevice device : smartHome.devices) {
                res += device.controllers.size();
            }
            return res;
        }

        @Nullable
        private ArduinoDevice getDeviceByControllerPosition(int position) {
            int skippedContrCount = 0;
            for (ArduinoDevice device : smartHome.devices) {
                // 2 contr
                // 3 contr
                // pos=4
                int thisContrCount = device.controllers.size();
                if (position <= skippedContrCount + thisContrCount - 1) {
                    return device;
                }
                skippedContrCount += thisContrCount;
            }
            return null;
        }

        // TODO: 12/20/18 need better solution, but not now!
        @Nullable
        private Controller getControllerByPosition(int position) {
            int skippedContrCount = 0;
            for (ArduinoDevice device : smartHome.devices) {
                int thisContrCount = device.controllers.size();
                if (position <= skippedContrCount + thisContrCount - 1) {
                    return device.controllers.get(position - skippedContrCount);
                }
                skippedContrCount += thisContrCount;
            }
            return null;
        }
    }

    private static class ControllerViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
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
                String message = null;
                message = "Returned code: " + response.code();
                try {
                     message += ", " + response.raw().body().string();
                } catch (Exception ignored) {}
                Toast.makeText(guid.getContext(), message, Toast.LENGTH_LONG).show();
            }
            endStateChange();
        }
    }
}