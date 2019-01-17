package smarthome.datalibrary.database.store.firebase;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import smarthome.datalibrary.database.store.RPInfoStorage;
import smarthome.datalibrary.database.store.listeners.RPInfoListener;

import static smarthome.datalibrary.database.constants.Constants.FIREBASE_READ_VALUE_ERROR;
import static smarthome.datalibrary.database.constants.Constants.RP_IP_REF;
import static smarthome.datalibrary.BuildConfig.DEBUG;

public class FirebaseRPInfoStorage implements RPInfoStorage {

    private static FirebaseRPInfoStorage instance;

    private DatabaseReference ipRef;


    public static FirebaseRPInfoStorage getInstance() {
        if(instance == null)
            instance = instantiate();

        return instance;
    }

    private static FirebaseRPInfoStorage instantiate() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() == null)
            return null;

        else return new FirebaseRPInfoStorage(auth.getCurrentUser().getUid());
    }

    private FirebaseRPInfoStorage(String uid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        ipRef = database.getReference()
                .child(uid)
                .child(RP_IP_REF);

    }

    @Override
    public void postRaspberryIp(String ip) {
        ipRef.setValue(ip);
    }

    @Override
    public void postRaspberryIp(String ip, DatabaseReference.CompletionListener listener) {
        ipRef.setValue(ip, listener);
    }

    @Override
    public void getRaspberryInfo(final RPInfoListener listener) {
        ipRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onRaspberryIpReceived(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(DEBUG) Log.d(instance.getClass().getName(), FIREBASE_READ_VALUE_ERROR, databaseError.toException());
            }
        });
    }
}
