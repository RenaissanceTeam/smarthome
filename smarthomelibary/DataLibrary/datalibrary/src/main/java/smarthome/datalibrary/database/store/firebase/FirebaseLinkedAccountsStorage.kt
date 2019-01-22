package smarthome.datalibrary.database.store.firebase

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import smarthome.datalibrary.database.constants.Constants.FIREBASE_READ_VALUE_ERROR
import smarthome.datalibrary.database.constants.Constants.LINKED_ACCS_REF
import smarthome.datalibrary.database.model.LinkedAccounts
import smarthome.datalibrary.database.store.LinkedAccountsStorage
import smarthome.datalibrary.database.store.listeners.LinkedAccountsListener

class FirebaseLinkedAccountsStorage(private val uid: String,
                                    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : LinkedAccountsStorage {

    private val ref: DocumentReference = db.collection(uid).document(LINKED_ACCS_REF)

    override fun postLinkedAccounts(
        linkedAccounts: LinkedAccounts,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        ref.set(linkedAccounts)
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun getLinkedAccounts(listener: LinkedAccountsListener) {
        ref.get()
            .addOnSuccessListener { res ->
                res.toObject(LinkedAccounts::class.java)?.let { listener.onLinkedAccountsReceived(it) }
            }
            .addOnFailureListener { exception -> Log.d(javaClass.name, FIREBASE_READ_VALUE_ERROR, exception) }
    }

    companion object {

        private var instance: FirebaseLinkedAccountsStorage? = null


        fun getInstance(): FirebaseLinkedAccountsStorage? {
            if (instance == null)
                instance = instantiate()

            return instance
        }

        private fun instantiate(): FirebaseLinkedAccountsStorage? {
            val auth = FirebaseAuth.getInstance()

            auth.currentUser?.let { return FirebaseLinkedAccountsStorage(it.uid) } ?: return null
        }
    }
}
