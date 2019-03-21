package smarthome.raspberry.thirdpartydevices;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.GatewayService;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Readable;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Writable;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Gateway;
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice;
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.ToggleController;
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.discover.DeviceDetector;

import static smarthome.library.common.constants.ControllerTypesKt.GATEWAY_HUMIDITY_CONTROLLER;
import static smarthome.library.common.constants.ControllerTypesKt.GATEWAY_LIGHT_ON_OFF_CONTROLLER;
import static smarthome.library.common.constants.ControllerTypesKt.GATEWAY_PRESSURE_CONTROLLER;
import static smarthome.library.common.constants.ControllerTypesKt.GATEWAY_RGB_CONTROLLER;
import static smarthome.library.common.constants.ControllerTypesKt.GATEWAY_TEMPERATURE_CONTROLLER;
import static smarthome.library.common.constants.DeviceTypes.WEATHER_SENSOR_TYPE;
import static smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.ConstantsKt.STATUS_OFF;
import static smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.ConstantsKt.STATUS_ON;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Tests {

    private final String TAG = getClass().getName();

    public GatewayService gatewayService;

    public void initGateway() {
        Log.i(TAG, "Setting up environment...");

        gatewayService = GatewayService.builder()
                .setGatewayPassword("tl3o393ndev67kv2")
                .build();
    }

    @Test
    public void testGatewayDevicesDiscovering() {
        initGateway();

        try {
            TimeUnit.MILLISECONDS.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Gateway gateway = gatewayService.getGateway();

        Log.v(TAG, gateway.toString());

        Log.d(TAG, "\n\n\nREADY TO DISCOVER DEVICES\n\n\n");

        gatewayService.discover();

        try {
            TimeUnit.MILLISECONDS.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGateWay() {
        initGateway();

        try {
            TimeUnit.MILLISECONDS.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Gateway gateway = gatewayService.getGateway();

        Log.v(TAG, gateway.toString());

        ((Writable) gateway.getControllerByType(GATEWAY_LIGHT_ON_OFF_CONTROLLER)).write(STATUS_ON);

        try {
            TimeUnit.MILLISECONDS.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ((Writable) gateway.getControllerByType(GATEWAY_RGB_CONTROLLER)).write(255, 0, 0);

        try {
            TimeUnit.MILLISECONDS.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ((Writable) gateway.getControllerByType(GATEWAY_RGB_CONTROLLER)).write(0, 255, 0);

        try {
            TimeUnit.MILLISECONDS.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ((Writable) gateway.getControllerByType(GATEWAY_RGB_CONTROLLER)).write(0, 0, 255);

        try {
            TimeUnit.MILLISECONDS.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ((Writable) gateway.getControllerByType(GATEWAY_LIGHT_ON_OFF_CONTROLLER)).write(STATUS_OFF);

        List<smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice> initedDevices = gatewayService.getDevices();
        String weather = "Temperature: " +  ((Readable) gatewayService.getDeviceByType(WEATHER_SENSOR_TYPE).getControllerByType(GATEWAY_TEMPERATURE_CONTROLLER)).read() +
                ", Humidity: " +  ((Readable) gatewayService.getDeviceByType(WEATHER_SENSOR_TYPE).getControllerByType(GATEWAY_HUMIDITY_CONTROLLER)).read() +
                ", Pressure: " +  ((Readable) gatewayService.getDeviceByType(WEATHER_SENSOR_TYPE).getControllerByType(GATEWAY_PRESSURE_CONTROLLER)).read();
        Log.v(TAG, weather);
        System.out.println("Test finished");

    }

    @Test
    public void testYeelight() {
        Set<YeelightDevice> devices = DeviceDetector.Companion.getInstance().discover();
        YeelightDevice bulb = (YeelightDevice) devices.toArray()[0];
        Log.v(TAG, bulb.toString());
        ((ToggleController)bulb.controllers.get(2)).write();
    }

    /*@Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("raspberry.smarthome.thirdpartydevices.test", appContext.getPackageName());
    }*/
}
