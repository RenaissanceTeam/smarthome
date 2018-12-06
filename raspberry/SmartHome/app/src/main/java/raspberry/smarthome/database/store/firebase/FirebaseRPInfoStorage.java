package raspberry.smarthome.database.store.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import raspberry.smarthome.database.Constants;
import raspberry.smarthome.database.model.RaspberryInfo;
import raspberry.smarthome.database.store.RPInfoStorage;
import raspberry.smarthome.database.store.listeners.RPInfoListener;

import static raspberry.smarthome.MainActivity.DEBUG;

public class FirebaseRPInfoStorage implements RPInfoStorage {

    private static FirebaseRPInfoStorage instance;

    private DatabaseReference ref;


    public FirebaseRPInfoStorage getInstance() {
        if(instance == null)
            instance = instantiate();

        return instance;
    }

    private FirebaseRPInfoStorage instantiate() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() == null)
            return null;

        else return new FirebaseRPInfoStorage(auth.getCurrentUser().getUid());
    }

    private FirebaseRPInfoStorage(String uid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        ref = database.getReference()
                .child(uid)
                .child(Constants.RP_INFO_REF);
    }

    @Override
    public void postRaspberryInfo(RaspberryInfo info) {
        ref.setValue(info);
    }

    @Override
    public void postRaspberryInfo(RaspberryInfo info, DatabaseReference.CompletionListener listener) {
        ref.setValue(info, listener);
    }

    @Override
    public void getRaspberryInfo(final RPInfoListener listener) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onRaspberryInfoReceived(dataSnapshot.getValue(RaspberryInfo.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(DEBUG) Log.d(instance.getClass().getName(), Constants.FIREBASE_READ_VALUE_ERROR, databaseError.toException());
            }
        });
    }
}
