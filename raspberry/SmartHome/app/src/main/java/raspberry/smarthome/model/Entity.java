package raspberry.smarthome.model;

public class Entity {


    private long GUID;

    public String name;

    public String description;


    public Entity() {
        GUID = 1;
    }

    public long getGUID() {
        return GUID;
    }
}
