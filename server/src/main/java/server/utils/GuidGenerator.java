package server.utils;

import java.time.LocalDate;
import java.util.Random;


public class GuidGenerator {

    private GuidStorage storage;

    private LocalDate date;

    public GuidGenerator(){
        storage = GuidStorage.getInstance();
    }

    public long issuenewguid(){
        date = LocalDate.now();

        long res = 0;

        boolean isUnique = false;

        while (!isUnique){
            res = generateNext();
            isUnique = storage.addGuid(res);
        }

        return generateNext();
    }

    private long generateNext(){
        Random r = new Random();
        return Long.parseLong("" + r.nextInt() + date.getDayOfMonth() + date.getMonthValue() + date.getYear());
    }
}
