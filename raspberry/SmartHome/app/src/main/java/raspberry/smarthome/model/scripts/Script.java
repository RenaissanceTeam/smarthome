package raspberry.smarthome.model.scripts;

import raspberry.smarthome.model.Entity;

public class Script extends Entity {

    @Override
    protected long generateGUID(String... params) {
        return 123;
    }
}
