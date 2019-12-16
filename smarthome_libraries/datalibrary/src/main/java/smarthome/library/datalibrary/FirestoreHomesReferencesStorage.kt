package smarthome.library.datalibrary

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import smarthome.library.common.HomesReferences
import smarthome.library.common.util.delegates.DependOnChangeable
import smarthome.library.datalibrary.api.HomesReferencesStorage
import smarthome.library.datalibrary.api.boundary.UserIdHolder
import smarthome.library.datalibrary.constants.ACCOUNTS_NODE
import smarthome.library.datalibrary.constants.ACCOUNT_HOMES_ARRAY_REF
import smarthome.library.datalibrary.constants.HOMES_NODE
import smarthome.library.datalibrary.util.withContinuation
import smarthome.library.datalibrary.util.withObjectContinuation
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreHomesReferencesStorage(userIdHolder: UserIdHolder) : HomesReferencesStorage {
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val ref: DocumentReference by DependOnChangeable(userIdHolder) {
        db.collection(ACCOUNTS_NODE).document(it)
    }

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
        val homeExists = try {
            ref.get().await().exists()
        } catch (e: FirebaseException) {
            false
        }
        
        if (homeExists) {
                taskUpdateHomeReference(homeReference)
        } else {
                taskNewHomeReference(homeReference)
        }.await()
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