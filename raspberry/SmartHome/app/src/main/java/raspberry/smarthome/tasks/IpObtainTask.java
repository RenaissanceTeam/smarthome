package raspberry.smarthome.tasks;

import android.os.AsyncTask;

import raspberry.smarthome.utils.ip.IpFinder;
import raspberry.smarthome.utils.ip.IpObserver;
import smarthome.datalibrary.database.store.RPInfoStorage;
import smarthome.datalibrary.database.store.firebase.FirebaseRPInfoStorage;

public class IpObtainTask extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... voids) {
        IpObserver observer = new IpFinder();

        return observer.obtainIp();
    }

    @Override
    protected void onPostExecute(String ip) {
        if (ip == null)
            return;

        RPInfoStorage storage = FirebaseRPInfoStorage.getInstance();
        if (storage != null) {
            storage.postRaspberryIp(ip);
        }
    }
}
