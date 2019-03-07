package smarthome.raspberry.model;

public abstract class Entity {

    public long GUID;

    public String name;

    public String description;

    public Entity() {
        GUID = -1;
    }

    protected abstract long generateGUID(String... params);
}
