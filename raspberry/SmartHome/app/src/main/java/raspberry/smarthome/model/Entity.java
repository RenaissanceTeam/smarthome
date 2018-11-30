package raspberry.smarthome.model;

public abstract class Entity {

    public String name;
    public String description;
    public long GUID;

    public Entity() {
        GUID = -1;
    }

    protected abstract long generateGUID(String... params);
}
