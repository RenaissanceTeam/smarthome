package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command;

public class DiscoverGatewayCmd extends Command {

    @Override
    public String toString() {
        return "{\"cmd\":\"get_id_list\"}";
    }
}
