package smarthome.raspberry.thirdpartydevices;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.GatewayEnv;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Gateway;
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device;
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.Controller;
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.ToggleController;
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.discover.DeviceDetector;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Tests {

    private final String TAG = getClass().getName();

    public GatewayEnv env;

    public void initGateway() {
        Log.i(TAG, "Setting up environment...");

        env = GatewayEnv.builder()
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

        Gateway gateway = env.getGateway();

        Log.v(TAG, gateway.toString());

        Log.d(TAG, "\n\n\nREADY TO DISCOVER DEVICES\n\n\n");

        env.discover();

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

        Gateway gateway = env.getGateway();

        Log.v(TAG, gateway.toString());

        gateway.enableLight();

        try {
            TimeUnit.MILLISECONDS.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        gateway.enableLight((byte) 255, (byte) 0, (byte) 0, 1000);

        try {
            TimeUnit.MILLISECONDS.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        gateway.enableLight((byte) 0, (byte) 255, (byte) 0, 1000);

        try {
            TimeUnit.MILLISECONDS.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        gateway.enableLight((byte) 0, (byte) 0, (byte) 255, 1000);

        try {
            TimeUnit.MILLISECONDS.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        gateway.disableLight();

    }

    @Test
    public void testYeelight() {
        Set<Device> devices = DeviceDetector.Companion.getInstance().discover();
        Device bulb = (Device) devices.toArray()[0];
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
