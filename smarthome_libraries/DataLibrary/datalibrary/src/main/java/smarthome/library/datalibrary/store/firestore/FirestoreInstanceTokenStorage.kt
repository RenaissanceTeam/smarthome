package smarthome.library.datalibrary.store.firestore

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import smarthome.library.datalibrary.constants.HOMES_NODE
import smarthome.library.datalibrary.constants.HOME_USERS_NODE
import smarthome.library.datalibrary.model.InstanceToken
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
        usersRef.document(userId).set(InstanceToken(token))
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun getToken(
        userId: String,
        successListener: OnSuccessListener<String>,
        failureListener: OnFailureListener
    ) {
        usersRef.document(userId).get()
            .addOnSuccessListener { successListener.onSuccess(it.toObject(InstanceToken::class.java)?.token) }
            .addOnFailureListener(failureListener)
    }

    override fun removeToken(
        userId: String,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        usersRef.document(userId).delete()
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }
}