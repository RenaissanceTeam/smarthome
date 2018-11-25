package ru.smarthome.network;

import ru.smarthome.network.api.Client;
import ru.smarthome.network.api.NetworkService;
import ru.smarthome.network.calls.ApiResponse;
import ru.smarthome.network.calls.OnApiResponse;
import ru.smarthome.network.model.RaspberryInfo;
import ru.smarthome.network.model.User;


public class ApiFetcher {

    private static ApiFetcher fetcher;

    public static ApiFetcher getInstance() {
        if (fetcher == null)
            fetcher = new ApiFetcher();

        return fetcher;
    }

    private ApiFetcher() {

    }

    public void getUser(OnApiResponse<User> listener, String email) {
        Client client = NetworkService.createService(Client.class);

        client.getUser(email).enqueue(new ApiResponse<>(listener, User.class));
    }

    public void getUserById(OnApiResponse<User> listener, long id) {
        Client client = NetworkService.createService(Client.class);

        client.getUserById(id).enqueue(new ApiResponse<>(listener, User.class));
    }


    public void getRaspberryInfo(OnApiResponse<RaspberryInfo> listener, long id) {
        Client client = NetworkService.createService(Client.class);

        client.getRaspberryInfo(id).enqueue(new ApiResponse<>(listener, RaspberryInfo.class));
    }

    public void updateUser(OnApiResponse<Void> listener, User user) {
        Client client = NetworkService.createService(Client.class);

        client.updateUser(user).enqueue(new ApiResponse<>(listener, Void.class));
    }

    public void createUser(OnApiResponse<Long> listener, User user) {
        Client client = NetworkService.createService(Client.class);

        client.createUser(user).enqueue(new ApiResponse<>(listener, Long.class));
    }

    public void updateRaspberryInfo(OnApiResponse<Void> listener, long userId, RaspberryInfo raspberryInfo) {
        Client client = NetworkService.createService(Client.class);

        client.updateRaspberryInfo(userId, raspberryInfo).enqueue(new ApiResponse<>(listener, Void.class));
    }

    public void deleteUser(OnApiResponse<Void> listener, long id) {
        Client client = NetworkService.createService(Client.class);

        client.deleteUser(id).enqueue(new ApiResponse<>(listener, Void.class));
    }

}
