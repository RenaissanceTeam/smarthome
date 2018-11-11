package raspberry.smarthome.model.device;

import raspberry.smarthome.model.Entity;

public abstract class IotDevice extends Entity {

    public abstract boolean connect();

    public abstract boolean disconnect();

}
