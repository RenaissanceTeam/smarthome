package smarthome.library.common.storage;

public class DbContract {
    public static final int VERSION = 2;

    public static class DbEntry {
        public static final String GUID_COLUMN = "guid";
        public static final String JSON_COLUMN = "json";
        public static final int GUID_INDEX = 0;
        public static final int JSON_INDEX = 1;

    }
}
