package smarthome.raspberry.utils

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import smarthome.library.datalibrary.store.HomesReferencesStorage
import smarthome.library.datalibrary.store.InstanceTokenStorage
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.library.datalibrary.store.firestore.FirestoreHomesReferencesStorage
import smarthome.library.datalibrary.store.firestore.FirestoreInstanceTokenStorage
import smarthome.library.datalibrary.store.firestore.FirestoreSmartHomeStorage
import smarthome.library.datalibrary.store.listeners.HomeExistenceListener
import smarthome.raspberry.BuildConfig.DEBUG
import smarthome.raspberry.UnableToCreateHomeStorage
import smarthome.raspberry.model.SmartHomeRepository
import smarthome.raspberry.service.DeviceObserver
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random

private const val HOME_ID_PREFIX = "home_id"
private const val TAG = "HomeController"

class HomeController(context: Context) {
    val sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context)
    val storage: HomesReferencesStorage = FirestoreHomesReferencesStorage.getInstance()!!
    private val tokenStorage: InstanceTokenStorage? = null

    suspend fun getHomeId(): String {
        if (!sharedPreferencesHelper.isHomeIdExists()) {
            saveNewHomeId()
        }
        return sharedPreferencesHelper.getHomeId()
    }

    fun getSmartHomeStorage(homeId: String): FirestoreSmartHomeStorage {
        return FirestoreSmartHomeStorage.getInstance(homeId)
                ?: throw UnableToCreateHomeStorage(homeId)
    }

    private suspend fun saveNewHomeId() {
        val homeId = generateFirestoreUniqueHomeId()
        sharedPreferencesHelper.setHomeId(homeId)

        createHomeReference(homeId)
        createEmptyHomeNode(homeId)
    }

    private suspend fun createHomeReference(homeId: String) {
        suspendCoroutine<Unit> { continuation ->
            storage.addHomeReference(
                    homeId,
                    OnSuccessListener {
                        if (DEBUG) Log.d(TAG, "new homeId added: $homeId")
                        continuation.resumeWith(Result.success(Unit))
                    },
                    OnFailureListener {
                        if (DEBUG) Log.d(TAG, "adding home reference failed", it)
                        continuation.resumeWith(Result.failure(it))
                    }
            )
        }
    }

    private suspend fun createEmptyHomeNode(homeId: String) {
        suspendCoroutine<Unit> { continuation ->
            getSmartHomeStorage(homeId).createSmartHome(
                    OnSuccessListener {
                        if (DEBUG) Log.d(TAG, "empty home node successfully created")
                        continuation.resumeWith(Result.success(Unit))
                    },
                    OnFailureListener {
                        if (DEBUG) Log.d(TAG, "failed to create empty home node", it)
                        continuation.resumeWith(Result.failure(it))
                    }
            )
        }
    }

    private suspend fun generateFirestoreUniqueHomeId(): String {
        if (DEBUG) Log.d(TAG, "generateFirestoreUniqueHomeId")
        var homeId: String
        do {
            homeId = generateHomeId()
            val isUnique = suspendCoroutine<Boolean> { continuation ->
                storage.checkIfHomeExists(homeId, object : HomeExistenceListener {
                    override fun onHomeAlreadyExists() = continuation.resumeWith(Result.success(false))
                    override fun onHomeDoesNotExist() = continuation.resumeWith(Result.success(true))
                })
            }
        } while (!isUnique)
        if (DEBUG) Log.d(TAG, "generated homeid=$homeId, that is unique")
        return homeId
    }

    private fun generateHomeId(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val currentTime = LocalDateTime.now().format(formatter)

        val randomPart = Random.nextInt(0, 9999).toString()

        return "$HOME_ID_PREFIX$currentTime$randomPart"
    }

    fun getTokenStorage(homeId: String): InstanceTokenStorage {
        return tokenStorage ?: FirestoreInstanceTokenStorage(homeId)
    }
}