package smarthome.client.util

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import smarthome.client.AuthenticationFailed
import smarthome.client.NoHomeid
import smarthome.client.RemoteFailure
import smarthome.library.common.SmartHome
import smarthome.library.datalibrary.store.InstanceTokenStorage
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.library.datalibrary.store.firestore.FirestoreHomesReferencesStorage
import smarthome.library.datalibrary.store.firestore.FirestoreInstanceTokenStorage
import smarthome.library.datalibrary.store.firestore.FirestoreSmartHomeStorage
import smarthome.library.datalibrary.store.listeners.HomesReferencesListener
import smarthome.library.datalibrary.store.listeners.SmartHomeListener
import kotlin.coroutines.suspendCoroutine

object CloudStorages {
    private var homeStorage: SmartHomeStorage? = null
    private var instanceTokenStorage: InstanceTokenStorage? = null
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    suspend fun getSmartHomeStorage(): SmartHomeStorage {
        if (homeStorage == null) {
            setupFirestore()
        }
        return homeStorage!!
    }

    suspend fun getInstanceTokenStorage(): InstanceTokenStorage {
        if (instanceTokenStorage == null) {
            setupFirestore()
        }
        return instanceTokenStorage!!
    }

    private suspend fun setupFirestore() {
        val userId = this.userId ?: throw AuthenticationFailed()

        val references = FirestoreHomesReferencesStorage(userId)
        val homeIds = suspendCoroutine<List<String>> { continuation ->
            references.getHomesReferences(
                    HomesReferencesListener { continuation.resumeWith(Result.success(it.homes)) },
                    OnFailureListener { continuation.resumeWith(Result.failure(RemoteFailure(it))) }
            )
        }
        if (homeIds.isNullOrEmpty()) throw NoHomeid()
        val homeId = homeIds[0] // todo let user choose

        homeStorage = FirestoreSmartHomeStorage(homeId)
        instanceTokenStorage = FirestoreInstanceTokenStorage(homeId)

        FcmTokenRequester().initFcmToken()
    }

    suspend fun loadHome(): SmartHome {
        val storage = getSmartHomeStorage()

        return suspendCoroutine { continuation ->
            storage.getSmartHome(
                    SmartHomeListener {
                        continuation.resumeWith(Result.success(it))
                    },
                    OnFailureListener { continuation.resumeWith(Result.failure(RemoteFailure(it))) }
            )
        }
    }

    suspend fun saveInstanceToken(token: String) {
        val userId = this.userId ?: throw AuthenticationFailed()
        val storage = getInstanceTokenStorage()
        suspendCoroutine<Unit> { continuation ->
            storage.saveToken(userId, token,
                    OnSuccessListener { continuation.resumeWith(Result.success(Unit)) },
                    OnFailureListener { continuation.resumeWith(Result.failure(it)) })
        }
    }
}