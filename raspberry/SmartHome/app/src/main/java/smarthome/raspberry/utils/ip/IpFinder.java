package smarthome.raspberry.utils.ip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import static ru.smarthome.BuildConfig.DEBUG;

public class IpFinder implements IpObserver {

    @Override
    public String obtainIp() {
        URL whatismyip;
        String ip = null;

        try {
            whatismyip = new URL("http://checkip.amazonaws.com");

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            ip = in.readLine();

        } catch (IOException e) {
            if(DEBUG)e.printStackTrace();
        }


        return ip;
    }
}
