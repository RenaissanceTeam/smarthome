package smarthome.raspberry.model.scripts;

import smarthome.raspberry.model.Entity;

public class Script extends Entity {

    @Override
    protected long generateGUID(String... params) {
        return 123;
    }
}
