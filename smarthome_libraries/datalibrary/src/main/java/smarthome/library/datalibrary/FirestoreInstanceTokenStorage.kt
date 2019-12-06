package smarthome.library.datalibrary

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Observable
import smarthome.library.common.InstanceToken
import smarthome.library.common.util.delegates.DependOnChangeable
import smarthome.library.datalibrary.api.InstanceTokenStorage
import smarthome.library.datalibrary.api.boundary.HomeIdHolder
import smarthome.library.datalibrary.constants.HOMES_NODE
import smarthome.library.datalibrary.constants.HOME_USERS_NODE
import smarthome.library.datalibrary.util.withContinuation
import smarthome.library.datalibrary.util.withObjectContinuation
import kotlin.coroutines.suspendCoroutine

class FirestoreInstanceTokenStorage(homeIdHolder: HomeIdHolder) : InstanceTokenStorage {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersRef by DependOnChangeable(homeIdHolder) {
        db.collection(HOMES_NODE).document(it).collection(HOME_USERS_NODE)
    }

    override suspend fun saveToken(userId: String, token: String) {
        suspendCoroutine<Unit> {
            usersRef.document(userId).set(InstanceToken(token)).withContinuation(it)
        }
    }

    override suspend fun getToken(userId: String): String {
        return suspendCoroutine {
            usersRef.document(userId).get().withObjectContinuation(it)
        }
    }

    override suspend fun removeToken(userId: String) {
        suspendCoroutine<Unit> {
            usersRef.document(userId).delete().withContinuation(it)
        }
    }

    override fun observeTokenChanges(): Observable<String> {
        TODO("strange method, it listens to all added users, not their tokens")
//        return Observable.create {
//            usersRef.addSnapshotListener(EventListener { snapshot, e ->
//                if (e != null) {
//                    it.onError(e)
//                    return@EventListener
//                }
//
//                if (snapshot == null) {
//                    it.onError(NullPointerException("Null token snapshot"))
//                    return@EventListener
//                }
//
//
//                it.onNext(snapshot.map { it.toObject(InstanceToken::class.java) })
//            })
//        }
    }
}
