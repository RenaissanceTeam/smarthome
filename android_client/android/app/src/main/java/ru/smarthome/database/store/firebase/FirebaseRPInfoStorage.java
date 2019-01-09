package ru.smarthome.database.store.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.smarthome.database.store.RPInfoStorage;
import ru.smarthome.database.store.listeners.RPInfoListener;

import static ru.smarthome.BuildConfig.DEBUG;
import static ru.smarthome.constants.Constants.*;

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
