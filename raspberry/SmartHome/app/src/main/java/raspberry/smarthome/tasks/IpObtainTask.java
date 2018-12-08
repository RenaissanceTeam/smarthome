package raspberry.smarthome.tasks;

import android.os.AsyncTask;

import raspberry.smarthome.database.store.RPInfoStorage;
import raspberry.smarthome.database.store.firebase.FirebaseRPInfoStorage;
import raspberry.smarthome.utils.ip.IpFinder;
import raspberry.smarthome.utils.ip.IpObserver;

public class IpObtainTask extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... voids) {
        IpObserver observer = new IpFinder();

        return observer.obtainIp();
    }

    @Override
    protected void onPostExecute(String ip) {
        RPInfoStorage storage = FirebaseRPInfoStorage.getInstance();
        storage.postRaspberryIp(ip);
    }
}
