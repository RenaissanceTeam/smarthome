package raspberry.smarthome.thirdpartydevices.xiaomi.gateway;

import android.os.CancellationSignal;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

import static smarthome.library.common.constants.DeviceTypes.*;

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

    private final String TAG = getClass().getName();

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
        Log.i(TAG, "Processing report...");
        Optional.ofNullable(findDeviceBySid(response.sid)).ifPresent(d -> d.parseData(response.data));
    }

    private void processHeartBeat(ResponseCmd response) {
        Log.i(TAG,"processing heartbeat");
        if(gateway != null && response.sid.equals(gateway.getSid())) {
            transport.setCurrentToken(response.token);
            gateway.parseData(response.data);

            Log.i(TAG,"new token received: " + response.token);
        } else {
            Optional.ofNullable(findDeviceBySid(response.sid)).ifPresent(d -> d.parseData(response.data));
        }
    }

    private void updateDeviceList(ResponseCmd response) {
        Log.i(TAG, "Updating devices info");

        if(response.model.equals(GATEWAY_TYPE)) {
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
        Log.i(TAG, "Discovering devices");

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
            Log.d(TAG, "Waiting for messages");

            try {

                String s = transport.receive().get();

                Log.d(TAG,"Message received: " + s);

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
            Log.d(TAG,"Waiting for multicast messages");
            try {

                String s = new String(transport.getIncomingMulticastChannel().receive());

                Log.d(TAG,"Multicast message received: " + s);

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
        deviceDictionary.put(DOOR_WINDOW_SENSOR_TYPE, DoorWindowSensor::new);
        deviceDictionary.put(MOTION_SENSOR_TYPE, MotionSensor::new);
        deviceDictionary.put(WIRELESS_SWITCH_TYPE, sid -> new WirelessSwitch(sid, transport));
        deviceDictionary.put(TEMPERATURE_HUMIDITY_SENSOR_TYPE, THSensor::new);
        deviceDictionary.put(WATER_LEAK_SENSOR_TYPE, WaterLeakSensor::new);
        deviceDictionary.put(WEATHER_SENSOR_TYPE, WeatherSensor::new);
        deviceDictionary.put(WIRED_DUAL_WALL_SWITCH_TYPE, sid -> new WiredDualWallSwitch(sid, transport));
        deviceDictionary.put(WIRED_SINGLE_WALL_SWITCH_TYPE, sid -> new WiredSingleWallSwitch(sid, transport));
        deviceDictionary.put(SMART_PLUG_TYPE, sid -> new SmartPlug(sid, transport));
        deviceDictionary.put(SMOKE_SENSOR_TYPE, SmokeSensor::new);
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
        cs.cancel();
        super.finalize();
    }
}
