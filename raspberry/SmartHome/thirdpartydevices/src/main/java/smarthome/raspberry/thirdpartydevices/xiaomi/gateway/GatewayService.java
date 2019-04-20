package smarthome.raspberry.thirdpartydevices.xiaomi.gateway;

import android.os.CancellationSignal;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.DiscoverGatewayCmd;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.ReadDeviceCmd;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.ResponseCmd;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.DoorWindowSensor;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Gateway;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.MotionSensor;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.SmartPlug;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.SmokeSensor;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.THSensor;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.WaterLeakSensor;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.WeatherSensor;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.WiredDualWallSwitch;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.WiredSingleWallSwitch;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.WirelessSwitch;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.interfaces.TransportSettable;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport;

import static smarthome.library.common.constants.DeviceTypes.DOOR_WINDOW_SENSOR_TYPE;
import static smarthome.library.common.constants.DeviceTypes.GATEWAY_TYPE;
import static smarthome.library.common.constants.DeviceTypes.MOTION_SENSOR_TYPE;
import static smarthome.library.common.constants.DeviceTypes.SMART_PLUG_TYPE;
import static smarthome.library.common.constants.DeviceTypes.SMOKE_SENSOR_TYPE;
import static smarthome.library.common.constants.DeviceTypes.TEMPERATURE_HUMIDITY_SENSOR_TYPE;
import static smarthome.library.common.constants.DeviceTypes.WATER_LEAK_SENSOR_TYPE;
import static smarthome.library.common.constants.DeviceTypes.WEATHER_SENSOR_TYPE;
import static smarthome.library.common.constants.DeviceTypes.WIRED_DUAL_WALL_SWITCH_TYPE;
import static smarthome.library.common.constants.DeviceTypes.WIRED_SINGLE_WALL_SWITCH_TYPE;
import static smarthome.library.common.constants.DeviceTypes.WIRELESS_SWITCH_TYPE;

public class GatewayService {

    private Gateway gateway;
    private String gatewayPassword;
    private Map<String, GatewayDevice> devices = Collections.synchronizedMap(new HashMap<>());
    private UdpTransport transport;

    private Map<String, Consumer<ResponseCmd>> commandsToActions = new HashMap<>();
    private Map<String, Function<String, GatewayDevice>> deviceDictionary = new HashMap<>();

    private Gson gson = new Gson();
    private CancellationSignal cs = new CancellationSignal();
    private ExecutorService executor = Executors.newFixedThreadPool(2);

    private final int readInterval = 100;

    private final String TAG = getClass().getName();

    private GatewayService(Gateway gateway, List<GatewayDevice> devices) {
        this(gateway.getSid(), gateway.getPassword());
        this.gateway = gateway;
        gateway.setUpTransport(transport);
        gateway.recoverControllers();
        
        if (devices != null) {
            for(GatewayDevice device : devices) {
                if (TransportSettable.class.isAssignableFrom(device.getClass()))
                    ((TransportSettable) device).setUpTransport(transport);

                device.recoverControllers();

                save(device);
            }
        }
    }

    private GatewayService(String sid, String password) {
        initConsumersMap();
        initDeviceDictionary();

        gatewayPassword = password;

        transport = new UdpTransport(password);

        executor.submit(() -> startReceivingUpdates(cs));
        executor.submit(() -> startReceivingMulticastMessages(cs));

        transport.sendCommand(new DiscoverGatewayCmd());
    }

    public Gateway getGateway() {
        return gateway;
    }

    public List<GatewayDevice> getDevices() {
        return new ArrayList<>(devices.values());
    }

    public GatewayDevice getDeviceByType(String type) {
        for (GatewayDevice device : getDevices()) {
            if(device.getType().equals(type))
                return device;
        }

        return null;
    }

    public void discover() {
        transport.sendCommand(new DiscoverGatewayCmd());
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

        GatewayDevice device = findDeviceBySid(response.sid);

        if(device != null) return;

        device = deviceDictionary.get(response.model).apply(response.sid);

        if(response.data != null) device.parseData(response.data);

        save(device);
    }

    private void discover(ResponseCmd response) {
        Log.i(TAG, "Discovering devices");

        transport.setCurrentToken(response.token);

        if(gateway == null) {
            gateway = new Gateway(gatewayPassword, response.sid, transport);
            save(gateway);
        }

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

    private GatewayDevice findDeviceBySid(String sid) {
        synchronized (devices) {
            return devices.get(sid);
        }
    }

    private void save(GatewayDevice device) {
        synchronized (devices) {
            devices.put(device.getSid(), device);
        }
    }


    private void initConsumersMap() {
        commandsToActions.put("get_id_list_ack", this::discover);
        commandsToActions.put("read_ack", this::updateDeviceList);
        commandsToActions.put("heartbeat", this::processHeartBeat);
        commandsToActions.put("report", this::processReport);
    }

    private void initDeviceDictionary() {
        deviceDictionary.put(DOOR_WINDOW_SENSOR_TYPE, sid -> new DoorWindowSensor(sid, gateway.getSid()));
        deviceDictionary.put(MOTION_SENSOR_TYPE, sid -> new MotionSensor(sid, gateway.getSid()));
        deviceDictionary.put(WIRELESS_SWITCH_TYPE, sid -> new WirelessSwitch(sid, transport, gateway.getSid()));
        deviceDictionary.put(TEMPERATURE_HUMIDITY_SENSOR_TYPE, sid -> new THSensor(sid, TEMPERATURE_HUMIDITY_SENSOR_TYPE, gateway.getSid()));
        deviceDictionary.put(WATER_LEAK_SENSOR_TYPE, sid -> new WaterLeakSensor(sid, gateway.getSid(),null));
        deviceDictionary.put(WEATHER_SENSOR_TYPE, sid -> new WeatherSensor(sid, gateway.getSid()));
        deviceDictionary.put(WIRED_DUAL_WALL_SWITCH_TYPE, sid -> new WiredDualWallSwitch(sid, transport, gateway.getSid()));
        deviceDictionary.put(WIRED_SINGLE_WALL_SWITCH_TYPE, sid -> new WiredSingleWallSwitch(sid, transport, gateway.getSid()));
        deviceDictionary.put(SMART_PLUG_TYPE, sid -> new SmartPlug(sid, transport, gateway.getSid()));
        deviceDictionary.put(SMOKE_SENSOR_TYPE, sid -> new SmokeSensor(sid, gateway.getSid(), null));
    }

    public static GatewayService.Builder builder() {
        return new GatewayService.Builder();
    }

    public static class Builder {

        private Gateway gateway;
        List<GatewayDevice> devices;
        private String psw;
        private String sid;

        private Builder() {
        }

        public GatewayService.Builder setGatewayPassword(String psw) {
            this.psw = psw;

            return this;
        }

        public GatewayService.Builder setGatewaySid(String sid) {
            this.sid = sid;

            return this;
        }

        public GatewayService.Builder setGateway(Gateway gateway) {
            this.gateway = gateway;

            return this;
        }

        public GatewayService.Builder setDevices(List<GatewayDevice> devices) {
            this.devices = devices;

            return this;
        }

        public GatewayService build() {
            if(gateway != null)
                return new GatewayService(gateway, devices);

            return new GatewayService(sid, psw);
        }

    }

    public void kill() {
        cs.cancel();
        executor.shutdown();
        transport.kill();
        transport = null;
    }

    @Override
    protected void finalize() throws Throwable {
        cs.cancel();
        super.finalize();
    }
}
