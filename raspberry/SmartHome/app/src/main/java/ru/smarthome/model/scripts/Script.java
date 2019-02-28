package ru.smarthome.model.scripts;

import ru.smarthome.model.Entity;

public class Script extends Entity {

    @Override
    protected long generateGUID(String... params) {
        return 123;
    }
}
