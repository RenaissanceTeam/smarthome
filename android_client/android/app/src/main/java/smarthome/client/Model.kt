package smarthome.client

import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smarthome.library.common.IotDevice
import smarthome.library.common.SmartHome
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.library.datalibrary.store.firestore.FirestoreHomesReferencesStorage
import smarthome.library.datalibrary.store.firestore.FirestoreSmartHomeStorage
import smarthome.library.datalibrary.store.listeners.HomesReferencesListener
import smarthome.library.datalibrary.store.listeners.SmartHomeListener
import kotlin.coroutines.suspendCoroutine

// todo should not be a singleton
object Model {
    private val TAG = Model::class.java.simpleName
    private var homeStorage: SmartHomeStorage? = null
    private var smartHome: SmartHome? = null

    val raspberryApi: RaspberryApi
        get() {
            val gson = GsonBuilder().setLenient().create()

            val retrofit = Retrofit.Builder()
                    .baseUrl(RASPBERRY_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

            return retrofit.create(RaspberryApi::class.java)
        }

    suspend fun getDevice(guid: Long): IotDevice {
        var device = getDevices().find { it.guid == guid }
        if (device == null) {
            loadHome() // maybe local home instance is out of data // todo rethink this method later
            device = getDevices().find { it.guid == guid } ?: throw NoDeviceException(guid)
        }

        return device
    }

    suspend fun getDevices(): MutableList<IotDevice> {
        val home = smartHome ?: loadHome()
        return home.devices
    }

    private suspend fun loadHome(): SmartHome {
        val storage = homeStorage ?: setupFirestore()

        return suspendCoroutine { continuation ->
            val listener = SmartHomeListener {
                smartHome = it
                continuation.resumeWith(Result.success(it))
            }
            storage.getSmartHome(listener)
        }
    }

    private suspend fun setupFirestore(): SmartHomeStorage {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: throw AuthenticationFailed()

        val references = FirestoreHomesReferencesStorage(userId)
        val homeIds = suspendCoroutine<List<String>> { continuation ->
            references.getHomesReferences(HomesReferencesListener { continuation.resumeWith(Result.success(it.homes)) })
        }
        if (homeIds.isNullOrEmpty()) throw NoHomeid()

        val storage: SmartHomeStorage = FirestoreSmartHomeStorage(homeIds[0])
        homeStorage = storage
        return storage
    }
}