package smarthome.library.datalibrary.store.firestore

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import smarthome.library.datalibrary.constants.ACCOUNTS_NODE
import smarthome.library.datalibrary.constants.ACCOUNT_HOMES_ARRAY_REF
import smarthome.library.datalibrary.constants.HOMES_NODE
import smarthome.library.datalibrary.model.HomesReferences
import smarthome.library.datalibrary.store.HomesReferencesStorage
import smarthome.library.datalibrary.util.withContinuation
import smarthome.library.datalibrary.util.withObjectContinuation
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreHomesReferencesStorage(
    uid: String,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : HomesReferencesStorage {

    private val ref: DocumentReference = db.collection(ACCOUNTS_NODE).document(uid)


    override suspend fun replaceHomesReferences(homesReferences: HomesReferences) {
        suspendCoroutine<Unit> {
            ref.set(homesReferences).withContinuation(it)
        }
    }

    override suspend fun mergeHomesReferences(updatedHomesReferences: HomesReferences) {
        suspendCoroutine<Unit> {
            ref.update(ACCOUNT_HOMES_ARRAY_REF, FieldValue.arrayUnion(updatedHomesReferences)).withContinuation(it)
        }
    }

    override suspend fun addHomeReference(homeReference: String) {
        val homeSnapshot = suspendCoroutine<DocumentSnapshot> { continuation ->
            ref.get()
                .addOnSuccessListener { continuation.resumeWith(Result.success(it)) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }


        suspendCoroutine<Unit> { continuation ->
            val task = if (homeSnapshot.exists()) {
                taskUpdateHomeReference(homeReference)
            } else {
                taskNewHomeReference(homeReference)
            }

            task.withContinuation(continuation)
        }
    }

    private fun taskUpdateHomeReference(homeReference: String) =
        ref.update(ACCOUNT_HOMES_ARRAY_REF, FieldValue.arrayUnion(homeReference))

    private fun taskNewHomeReference(homeReference: String): Task<Void> {
        return ref.set(HomesReferences(mutableListOf(homeReference)))
    }


    override suspend fun getHomesReferences(): HomesReferences {
        return suspendCoroutine {
            ref.get().withObjectContinuation(it)
        }
    }

    override suspend fun checkIfHomeExists(homeId: String): Boolean {
        return suspendCoroutine { continuation ->
            db.collection(HOMES_NODE).document(homeId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    continuation.resumeWith(Result.success(documentSnapshot.exists()))
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }

    }
}