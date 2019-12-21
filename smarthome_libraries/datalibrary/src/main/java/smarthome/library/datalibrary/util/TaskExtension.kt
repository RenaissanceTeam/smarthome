package smarthome.library.datalibrary.util

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import kotlin.coroutines.Continuation
import kotlin.coroutines.resumeWithException

fun <T> Task<T>.withContinuation(continuation: Continuation<Unit>) {
    this.addOnSuccessListener { continuation.resumeWith(Result.success(Unit)) }
        .addOnFailureListener { continuation.resumeWithException(it) }
}

inline fun <reified U> Task<DocumentSnapshot>.withObjectContinuation(continuation: Continuation<U>) {
    this.addOnSuccessListener {
        try {
            continuation.resumeWith(
                Result.success(
                    it.toObject(U::class.java)
                        ?: throw NullPointerException("Received null reference")
                )
            )
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }.addOnFailureListener { continuation.resumeWithException(it) }
}