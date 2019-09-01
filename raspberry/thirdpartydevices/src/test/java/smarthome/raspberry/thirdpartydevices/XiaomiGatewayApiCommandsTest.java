package smarthome.raspberry.thirdpartydevices;

import org.junit.Before;
import org.junit.Test;

import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.GatewayLightCmd;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.ReadDeviceCmd;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.SmartPlugCmd;
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.WriteCmd;

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
        System.out.println("SmartPlugCmd: " + new SmartPlugCmd("off").toString());
    }

    @Test
    public void printWriteCmd() {
        System.out.println("WriteCmd: " + new WriteCmd("sdfasASD", "type", "data").toString());
    }

}
