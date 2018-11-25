package raspberry.smarthome.model;

public class Entity {

    public String name;
    public String description;
    private long GUID;

    public Entity() {
        GUID = 1;
    }

    public long getGUID() {
        return GUID;
    }
}
