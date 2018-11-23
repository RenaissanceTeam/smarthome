package server.constants;

import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;

public class DBConstants {

    public static final String[] NON_ENTITY_EXTENDED_ELEMENTS = new String[] { };

    public static final HashMap<String, String> ELEMENT_COLLECTION_ELEMENTS = new HashMap<>();
    static {
        //ELEMENT_COLLECTION_ELEMENTS.put("", "");
    }

    @Value("${spring.datasource.username:server}")
    public static String USERNAME;

    @Value("${spring.datasource.password:password}")
    public static String PASSWORD;

    @Value("${spring.datasource.url:jdbc:postgresql://localhost:5432/smart_home}")
    public static String URL;
}
