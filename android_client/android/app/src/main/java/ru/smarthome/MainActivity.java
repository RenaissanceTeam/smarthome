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
import ru.smarthome.library.MyClass;
import ru.smarthome.library.RaspberryResponse;


import static ru.smarthome.constants.Constants.RC_SIGN_IN;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView controllers;
    private ControllersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout = findViewById(R.id.refresh_controllers);
        controllers = findViewById(R.id.controllers);
        controllers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ControllersAdapter(getLayoutInflater());
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
    public static RaspberryApi getRaspberryApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SmartHome.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(RaspberryApi.class);
    }
}