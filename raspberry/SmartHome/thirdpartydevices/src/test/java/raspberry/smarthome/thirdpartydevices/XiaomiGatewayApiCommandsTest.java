package raspberry.smarthome.thirdpartydevices;

import org.junit.Before;
import org.junit.Test;

import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.command.GatewayLightCmd;
import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.command.ReadDeviceCmd;
import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.command.SocketPlugCmd;
import raspberry.smarthome.thirdpartydevices.xiaomi.gateway.command.WriteCmd;

public class XiaomiGatewayApiCommandsTest {

    @Before public void insertNewLine() {
        System.out.println();
    }

    @Test
    public void printGatewayLightCmd() {
        System.out.println("GatewayLightCmd: " + new GatewayLightCmd(123L, 99).toString());
    }

    @Test
    public void printReadDeviceCmd() {
        System.out.println("ReadDeviceCmd: " + new ReadDeviceCmd("skdjhfkjshf").toString());
    }

    @Test
    public void printSocketPlugCmd() {
        System.out.println("SocketPlugCmd: " + new SocketPlugCmd(SocketPlugCmd.STATUS_OFF).toString());
    }

    @Test
    public void printWriteCmd() {
        System.out.println("WriteCmd: " + new WriteCmd("sdfasASD", "type", "data").toString());
    }

}
