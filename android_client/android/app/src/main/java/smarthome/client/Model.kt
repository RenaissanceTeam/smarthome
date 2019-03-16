package smarthome.client

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smarthome.library.common.BaseController
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

    suspend fun getController(guid: Long): BaseController {
        var controller: BaseController? = null
        getDevices().find {
            controller = it.controllers.find { it.guid == guid }
            controller != null
        }

        return controller ?: throw NoControllerException(guid)
    }

    suspend fun getDevice(guid: Long): IotDevice {
        return getDevices().find { it.guid == guid } ?: throw NoDeviceException(guid)
    }

    suspend fun getDevice(controller: BaseController): IotDevice {
        return getDevices().find {it.controllers.contains(controller)} ?: throw NoDeviceWithControllerException(controller)
    }

    suspend fun getDevices(): MutableList<IotDevice> {
        val home = getSmartHome()
        return home.devices
    }

    suspend fun changeController(device: IotDevice, controller: BaseController) {
        val storage = getSmartHomeStorage()
        suspendCoroutine<Unit> {continuation ->
            storage.updateDevice(device, controller,
                    OnSuccessListener { continuation.resumeWith(Result.success(Unit)) },
                    OnFailureListener { throw RemoteFailure(it) })
        }
    }

    private suspend fun getSmartHome() = smartHome ?: loadHome()

    private suspend fun loadHome(): SmartHome {
        val storage = getSmartHomeStorage()

        return suspendCoroutine { continuation ->
            val listener = SmartHomeListener {
                smartHome = it
                continuation.resumeWith(Result.success(it))
            }
            storage.getSmartHome(listener)
        }
    }

    private suspend fun getSmartHomeStorage() = homeStorage ?: setupFirestore()

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