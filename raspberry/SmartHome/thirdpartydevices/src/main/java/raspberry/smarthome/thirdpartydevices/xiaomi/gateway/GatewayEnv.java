package raspberry.smarthome.thirdpartydevices.xiaomi.gateway;

import android.os.CancellationSignal;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.command.DiscoverGatewayCmd;
import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.command.ReadDeviceCmd;
import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.command.ResponseCmd;
import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device.*;
import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.utils.UdpTransport;

public class GatewayEnv {

    private Gateway gateway;
    private List<Device> devices = Collections.synchronizedList(new ArrayList<>());
    private static UdpTransport transport;

    private Map<String, Consumer<ResponseCmd>> commandsToActions = new HashMap<>();
    private Map<String, Function<String, Device>> deviceDictionary = new HashMap<>();

    private Gson gson = new Gson();
    private CancellationSignal cs = new CancellationSignal();
    private ExecutorService executor = Executors.newFixedThreadPool(2);


    private final int readInterval = 100;

    private GatewayEnv(String sid, String password, boolean useCache) {
        initConsumersMap();
        initDeviceDictionary();

        transport = new UdpTransport(password);

        executor.submit(() -> startReceivingUpdates(cs));
        executor.submit(() -> startReceivingMulticastMessages(cs));

        transport.sendCommand(new DiscoverGatewayCmd());
    }

    public Gateway getGateway() {
        return gateway;
    }

    public List<Device> getDevices() {
        return devices;
    }

    private void processReport(ResponseCmd response) {
        System.out.println("Processing report...");
        try {
            Optional.of(findDeviceBySid(response.sid)).ifPresent(d -> d.parseData(response.data));
        } catch (Exception e) {
            System.out.println("FUCK UP");
            e.printStackTrace();
        }
        System.out.println("Report processed!");
    }

    private void processHeartBeat(ResponseCmd response) {
        System.out.println("processHeartBeat");
        if(gateway != null && response.sid.equals(gateway.getSid())) {
            transport.setCurrentToken(response.token);
            gateway.parseData(response.data);

            System.out.println("new token received: " + response.token);
        } else {
            Optional.of(findDeviceBySid(response.sid)).ifPresent(d -> d.parseData(response.data));
        }
    }

    private void updateDeviceList(ResponseCmd response) {
        if(response.model.equals(Device.GATEWAY_TYPE)) {
            if(gateway != null && gateway.getSid().equals(response.sid))
                gateway.parseData(response.data);

            return;
        }

        Device device = findDeviceBySid(response.sid);

        if(device != null) return;

        device = deviceDictionary.get(response.model).apply(response.sid);

        if(response.data != null) device.parseData(response.data);

        synchronized (devices) {
            devices.add(device);
        }
    }

    private void discover(ResponseCmd response) {
        System.out.println("Discovering...");

        transport.setCurrentToken(response.token);

        if(gateway == null)
            gateway = new Gateway(response.sid, transport);

        transport.sendCommand(new ReadDeviceCmd(response.sid));

        for(String sid : gson.fromJson(response.data, String[].class)) {
            transport.sendCommand(new ReadDeviceCmd(sid));

            try {
                TimeUnit.MILLISECONDS.sleep(readInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void startReceivingUpdates(CancellationSignal cs) {
        while (!cs.isCanceled()) {
            System.out.println("Waiting for messages");

            try {

                String s = transport.receive().get();

                System.out.println("GatewayEnv, message received: " + s);

                ResponseCmd responseCmd = gson.fromJson(s, ResponseCmd.class);

                if (!commandsToActions.containsKey(responseCmd.cmd)) continue;

                commandsToActions.get(responseCmd.cmd).accept(responseCmd);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void startReceivingMulticastMessages(CancellationSignal cs) {
        while (!cs.isCanceled()) {
            System.out.println("Waiting for heartbeat");
            try {

                String s = new String(transport.getIncomingMulticastChannel().receive());

                System.out.println("GatewayEnv, multicast message received: " + s);

                ResponseCmd responseCmd = gson.fromJson(s, ResponseCmd.class);

                if (!commandsToActions.containsKey(responseCmd.cmd)) continue;

                commandsToActions.get(responseCmd.cmd).accept(responseCmd);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Device findDeviceBySid(String sid) {
        synchronized (devices) {
            Device buff;
            Iterator<Device> it = devices.iterator();
            while (it.hasNext()) {
                buff = it.next();
                if(buff.getSid().equals(sid))
                    return buff;
            }
        }

        return null;
    }


    private void initConsumersMap() {
        commandsToActions.put("get_id_list_ack", this::discover);
        commandsToActions.put("read_ack", this::updateDeviceList);
        commandsToActions.put("heartbeat", this::processHeartBeat);
        commandsToActions.put("report", this::processReport);
    }

    private void initDeviceDictionary() {
        deviceDictionary.put(Device.DOOR_WINDOW_SENSOR_TYPE, DoorWindowSensor::new);
        deviceDictionary.put(Device.MOTION_SENSOR_TYPE, MotionSensor::new);
        deviceDictionary.put(Device.SWITCH_TYPE, Switch::new);
        deviceDictionary.put(Device.TEMPERATURE_HUMIDITY_SENSOR_TYPE, THSensor::new);
        deviceDictionary.put(Device.WATER_LEAK_SENSOR_TYPE, WaterLeakSensor::new);
        deviceDictionary.put(Device.WEATHER_SENSOR_TYPE, WeatherSensor::new);
        deviceDictionary.put(Device.WIRED_DUAL_WALL_SWITCH_TYPE, WiredDualWallSwitch::new);
        deviceDictionary.put(Device.WIRED_SINGLE_WALL_SWITCH_TYPE, WiredSingleWallSwitch::new);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String psw;
        private String sid;
        private boolean useCache = true;

        private Builder() {
        }

        public Builder setGatewayPassword(String psw) {
            this.psw = psw;

            return this;
        }

        public Builder setGatewaySid(String sid) {
            this.sid = sid;

            return this;
        }

        public Builder useCache(boolean useCache) {
            this.useCache = useCache;

            return this;
        }

        public GatewayEnv build() {
            return new GatewayEnv(sid, psw, useCache);
        }

    }

    @Override
    protected void finalize() throws Throwable {
        Objects.requireNonNull(cs).cancel();
        super.finalize();
    }
}
