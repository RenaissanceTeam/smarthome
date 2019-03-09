package smarthome.raspberry.utils

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import smarthome.library.datalibrary.store.HomesReferencesStorage
import smarthome.library.datalibrary.store.firestore.FirestoreHomesReferencesStorage
import smarthome.library.datalibrary.store.firestore.FirestoreSmartHomeStorage
import smarthome.library.datalibrary.store.listeners.HomeExistenceListener
import smarthome.raspberry.BuildConfig.DEBUG
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class HomeController(context: Context) {

    val sharedPreferencesHelper: SharedPreferencesHelper = SharedPreferencesHelper.getInstance(context)
    val storage: HomesReferencesStorage = FirestoreHomesReferencesStorage.getInstance()!!

    fun start() {
        if (DEBUG) Log.d(javaClass.name, "start")

        if (sharedPreferencesHelper.isHomeIdExists())
            return

        checkAndSaveHome()
    }

    private fun checkAndSaveHome() {
        if (DEBUG) Log.d(javaClass.name, "checkAndSaveHome")

        val homeId = generateHomeId()

        storage.checkIfHomeExists(homeId, object : HomeExistenceListener {
            override fun onHomeAlreadyExists() {
                if (DEBUG) Log.d(javaClass.name, "homeId already exists")
                checkAndSaveHome()
            }

            override fun onHomeDoesNotExist() {
                sharedPreferencesHelper.setHomeId(homeId)

                storage.addHomeReference(
                        homeId,
                        successListener = OnSuccessListener {
                            if (DEBUG) Log.d(javaClass.name, "new homeId added: $homeId")
                        },
                        failureListener = object : OnFailureListener {
                            override fun onFailure(p0: Exception) {
                                if (DEBUG) Log.d(javaClass.name, "adding home reference failed", p0)
                            }
                        })

                FirestoreSmartHomeStorage.getInstance(homeId)!!.createSmartHome(
                        successListener = OnSuccessListener {
                            if (DEBUG) Log.d(javaClass.name, "home node created")
                        }
                )
            }
        })
    }

    private fun generateHomeId(): String {
        var homeId = "home_id"

        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")

        homeId += now.format(formatter)
        homeId += Random.nextInt(0, 9999).toString()

        return homeId
    }

}