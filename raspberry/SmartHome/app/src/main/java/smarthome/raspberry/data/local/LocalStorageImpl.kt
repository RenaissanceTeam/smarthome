package smarthome.raspberry.data.local

import android.content.Context
import android.util.Log
import smarthome.library.datalibrary.store.HomesReferencesStorage
import smarthome.library.datalibrary.store.InstanceTokenStorage
import smarthome.library.datalibrary.store.firestore.FirestoreInstanceTokenStorage
import smarthome.library.datalibrary.store.firestore.FirestoreSmartHomeStorage
import smarthome.raspberry.BuildConfig
import smarthome.raspberry.data.LocalStorage
import smarthome.raspberry.utils.SharedPreferencesHelper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class LocalStorageImpl(private val context: Context): LocalStorage {
    private val HOME_ID_PREFIX = "home_id"
    private val TAG = "HomeController"
    private val sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context)
    private val input: LocalStorageInput = TODO()
    private val output: LocalStorageOutput = TODO()

    override suspend fun getHomeId(): String {
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

        output.createHome(homeId)
    }


    private suspend fun generateFirestoreUniqueHomeId(): String {
        if (BuildConfig.DEBUG) Log.d(TAG, "generateFirestoreUniqueHomeId")
        var homeId: String
        do {
            homeId = generateHomeId()
            val isUnique = storage.checkIfHomeExists(homeId)
        } while (!isUnique)
        if (BuildConfig.DEBUG) Log.d(TAG, "generated homeid=$homeId, that is unique")
        return homeId
    }

    private fun generateHomeId(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val currentTime = LocalDateTime.now().format(formatter)

        val randomPart = Random.nextInt(0, 9999).toString()

        return "$HOME_ID_PREFIX$currentTime$randomPart"
    }
}