package smarthome.library.datalibrary.store.firestore

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import smarthome.library.datalibrary.constants.HOMES_NODE
import smarthome.library.datalibrary.constants.HOME_USERS_NODE
import smarthome.library.datalibrary.constants.HOME_USERS_USER_TOKENS_REF
import smarthome.library.datalibrary.constants.TAG
import smarthome.library.datalibrary.model.InstanceTokenWrapper
import smarthome.library.datalibrary.store.InstanceTokenStorage

class FirestoreInstanceTokenStorage(
    homeId: String,
    db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : InstanceTokenStorage {

    private val usersRef = db.collection(HOMES_NODE).document(homeId).collection(HOME_USERS_NODE)

    override fun saveToken(
        userId: String,
        token: String,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        usersRef.document(userId).update(HOME_USERS_USER_TOKENS_REF, FieldValue.arrayUnion(token))
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun getTokens(
        userId: String,
        successListener: OnSuccessListener<List<String>>,
        failureListener: OnFailureListener
    ) {
        usersRef.document(userId).get()
            .addOnSuccessListener { successListener.onSuccess(it.toObject(InstanceTokenWrapper::class.java)?.tokens) }
            .addOnFailureListener(failureListener)
    }

    override fun getUsersToTokens(
        successListener: OnSuccessListener<List<Pair<String, InstanceTokenWrapper>>>,
        failureListener: OnFailureListener
    ) {
        usersRef.get()
            .addOnSuccessListener {
                val usersToTokens = mutableListOf<Pair<String, InstanceTokenWrapper>>()
                for (doc in it.documents) {
                    val user = doc.id
                    val tokens = doc.toObject(InstanceTokenWrapper::class.java) ?: continue
                    val userToTokens: Pair<String, InstanceTokenWrapper> = Pair(user, tokens)
                    usersToTokens.add(userToTokens)
                }
                successListener.onSuccess(usersToTokens)
            }
            .addOnFailureListener(failureListener)
    }

    override fun removeToken(
        userId: String,
        token: String,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        usersRef.document(userId).update(HOME_USERS_USER_TOKENS_REF, FieldValue.arrayRemove(token))
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun observeTokenChanges(observer: (List< Pair<String, InstanceTokenWrapper> >) -> Unit) {
        usersRef.addSnapshotListener(EventListener { snapshot, e ->
            if (e != null || snapshot == null) {
                Log.w(TAG, "Tokens updates listen failed", e)
                return@EventListener
            }

            observer(snapshot.map { Pair(it.id, it.toObject(InstanceTokenWrapper::class.java)) })
        })
    }
}