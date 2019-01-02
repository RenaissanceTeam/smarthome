package raspberry.smarthome.thirdpartydevices;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.GatewayEnv;
import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device.Gateway;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Tests {

    public GatewayEnv env;

    public void init() {
        System.out.println("Setting up environment...");

        env = GatewayEnv.builder()
                .setGatewayPassword("tl3o393ndev67kv2")
                .build();
    }

    @Test
    public void testGateWay() {
        init();

        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Gateway gateway = env.getGateway();

        System.out.println(gateway.toString());

        gateway.enableLight();

        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        gateway.disableLight();
    }

    /*@Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("raspberry.smarthome.thirdpartydevices.test", appContext.getPackageName());
    }*/
}
