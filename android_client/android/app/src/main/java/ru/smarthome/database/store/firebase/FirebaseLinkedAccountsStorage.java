package ru.smarthome.database.store.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.smarthome.database.Constants;
import ru.smarthome.database.model.LinkedAccounts;
import ru.smarthome.database.store.LinkedAccountsStorage;
import ru.smarthome.database.store.listeners.LinkedAccountsListener;

import static ru.smarthome.BuildConfig.DEBUG;

public class FirebaseLinkedAccountsStorage implements LinkedAccountsStorage {

    private static FirebaseLinkedAccountsStorage instance;

    private DatabaseReference ref;


    public static FirebaseLinkedAccountsStorage getInstance() {
        if(instance == null)
            instance = instantiate();

        return instance;
    }

    private static FirebaseLinkedAccountsStorage instantiate() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() == null)
            return null;

        else return new FirebaseLinkedAccountsStorage(auth.getCurrentUser().getUid());
    }

    private FirebaseLinkedAccountsStorage(String uid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        ref = database.getReference()
                .child(uid)
                .child(Constants.LINKED_ACCS_REF);
    }

    @Override
    public void postLinkedAccounts(LinkedAccounts linkedAccounts) {
        ref.setValue(linkedAccounts);
    }

    @Override
    public void postLinkedAccounts(LinkedAccounts linkedAccounts, DatabaseReference.CompletionListener listener) {
        ref.setValue(linkedAccounts, listener);
    }

    @Override
    public void getLinkedAccounts(final LinkedAccountsListener listener) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onLinkedAccountsReceived(dataSnapshot.getValue(LinkedAccounts.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(DEBUG) Log.d(instance.getClass().getName(), Constants.FIREBASE_READ_VALUE_ERROR, databaseError.toException());
            }
        });
    }
}
