package smarthome.library.datalibrary.store.firebase

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import smarthome.library.datalibrary.constants.LINKED_ACCOUNTS_REF
import smarthome.library.datalibrary.constants.defFailureListener
import smarthome.library.datalibrary.model.LinkedAccounts
import smarthome.library.datalibrary.store.LinkedAccountsStorage
import smarthome.library.datalibrary.store.listeners.LinkedAccountsListener

class FirebaseLinkedAccountsStorage(
    uid: String,
    db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : LinkedAccountsStorage {

    private val ref: DocumentReference = db.collection(uid).document(LINKED_ACCOUNTS_REF)

    override fun postLinkedAccounts(
        linkedAccounts: LinkedAccounts,
        successListener: OnSuccessListener<Void>, failureListener: OnFailureListener
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
            .addOnFailureListener(defFailureListener)
    }

    companion object {
        private var instance: FirebaseLinkedAccountsStorage? = null

        fun getInstance(): FirebaseLinkedAccountsStorage? {
            if (instance == null) {
                instance =
                    tryInstantiate()
            }
            return instance
        }

        private fun tryInstantiate(): FirebaseLinkedAccountsStorage? {
            return FirebaseAuth.getInstance().currentUser?.let {
                FirebaseLinkedAccountsStorage(
                    it.uid
                )
            }
        }
    }
}
