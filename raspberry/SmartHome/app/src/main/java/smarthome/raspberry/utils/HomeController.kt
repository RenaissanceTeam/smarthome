package smarthome.raspberry.utils

import android.content.Context
import android.util.Log
import smarthome.library.datalibrary.store.HomesReferencesStorage
import smarthome.library.datalibrary.store.InstanceTokenStorage
import smarthome.library.datalibrary.store.firestore.FirestoreInstanceTokenStorage
import smarthome.library.datalibrary.store.firestore.FirestoreSmartHomeStorage
import smarthome.raspberry.BuildConfig.DEBUG
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

private const val HOME_ID_PREFIX = "home_id"
private const val TAG = "HomeController"

class HomeController(context: Context) {
    val sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context)
    val storage: HomesReferencesStorage = TODO("inject")
    private val tokenStorage: InstanceTokenStorage? = null

    suspend fun getHomeId(): String {
        if (!sharedPreferencesHelper.isHomeIdExists()) {
            saveNewHomeId()
        }
        return sharedPreferencesHelper.getHomeId()
    }

    fun getSmartHomeStorage(homeId: String): FirestoreSmartHomeStorage {
        TODO("inject")
    }

    private suspend fun saveNewHomeId() {
        val homeId = generateFirestoreUniqueHomeId()
        sharedPreferencesHelper.setHomeId(homeId)

        createHomeReference(homeId)
        createEmptyHomeNode(homeId)
    }

    private suspend fun createHomeReference(homeId: String) {
        storage.addHomeReference(homeId)
    }

    private suspend fun createEmptyHomeNode(homeId: String) {
        getSmartHomeStorage(homeId).createSmartHome()
    }

    private suspend fun generateFirestoreUniqueHomeId(): String {
        if (DEBUG) Log.d(TAG, "generateFirestoreUniqueHomeId")
        var homeId: String
        do {
            homeId = generateHomeId()
            val isUnique = storage.checkIfHomeExists(homeId)
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