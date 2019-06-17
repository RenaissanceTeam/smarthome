package smarthome.library.datalibrary.store.firestore

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import smarthome.library.datalibrary.constants.ACCOUNTS_NODE
import smarthome.library.datalibrary.constants.ACCOUNT_HOMES_ARRAY_REF
import smarthome.library.datalibrary.constants.HOMES_NODE
import smarthome.library.datalibrary.model.HomesReferences
import smarthome.library.datalibrary.store.HomesReferencesStorage
import smarthome.library.datalibrary.store.listeners.HomesReferencesListener
import smarthome.library.datalibrary.store.listeners.OnHomeExists

class FirestoreHomesReferencesStorage(
    uid: String,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : HomesReferencesStorage {

    private val ref: DocumentReference = db.collection(ACCOUNTS_NODE).document(uid)

    override fun postHomesReferences(
        homesReferences: HomesReferences,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        ref.set(homesReferences)
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun addHomeReference(
        homeReference: String,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    ref.update(ACCOUNT_HOMES_ARRAY_REF, FieldValue.arrayUnion(homeReference))
                        .addOnSuccessListener(successListener)
                        .addOnFailureListener(failureListener)

                } else {
                    val data = HomesReferences()
                    data.homes.add(homeReference)
                    ref.set(data)
                        .addOnSuccessListener(successListener)
                        .addOnFailureListener(failureListener)
                }
            }
            .addOnFailureListener(failureListener)

    }

    override fun updateHomesReferences(
        updatedHomesReferences: HomesReferences,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        ref.update(ACCOUNT_HOMES_ARRAY_REF, FieldValue.arrayUnion(updatedHomesReferences))
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun getHomesReferences(
        listener: HomesReferencesListener,
        failureListener: OnFailureListener) {
        ref.get()
            .addOnSuccessListener { res ->
                res.toObject(HomesReferences::class.java)?.let { listener(it) }
            }
            .addOnFailureListener(failureListener)
    }

    override fun checkIfHomeExists(
        homeId: String,
        listener: OnHomeExists,
        failureListener: OnFailureListener
    ) {
        db.collection(HOMES_NODE).document(homeId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                listener(documentSnapshot.exists())
            }
            .addOnFailureListener(failureListener)
    }

    companion object {
        private var instance: FirestoreHomesReferencesStorage? = null

        fun getInstance(): FirestoreHomesReferencesStorage? {
            if (instance == null) {
                instance =
                    tryInstantiate()
            }
            return instance
        }

        private fun tryInstantiate(): FirestoreHomesReferencesStorage? {
            return FirebaseAuth.getInstance().currentUser?.let {
                FirestoreHomesReferencesStorage(
                    it.uid
                )
            }
        }
    }
}