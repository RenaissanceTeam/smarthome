package server.utils;

import server.constants.DBConstants;

import java.util.Set;


public class GuidStorage {


    private static GuidStorage instance;

    private Set<Long> guids;


    GuidStorage(){
        guids = new DBProvider(DBConstants.URL).getAllId();
    }


    public static GuidStorage getInstance() {
        if(instance == null)
            instance = new GuidStorage();

        return instance;
    }


    public boolean addGuid(long guid){
        return guids.add(guid);
    }

    public boolean contains(long guid){
        return guids.contains(guid);
    }

}
