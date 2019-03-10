package smarthome.client

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smarthome.client.constants.Constants
import smarthome.library.common.IotDevice
import smarthome.library.common.SmartHome
import smarthome.library.datalibrary.store.HomesReferencesStorage
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.library.datalibrary.store.firestore.FirestoreHomesReferencesStorage
import smarthome.library.datalibrary.store.firestore.FirestoreSmartHomeStorage
import smarthome.library.datalibrary.store.listeners.HomesReferencesListener
import smarthome.library.datalibrary.store.listeners.SmartHomeListener

// todo should not be a singleton
object Model {
    private val TAG = Model::class.java.simpleName
    private var homesReferences: HomesReferencesStorage? = null
    private var homeStorage: SmartHomeStorage? = null
    private var smartHome: SmartHome? = null

    private var firestoreReady = false
    private val firestoreWaiters = mutableListOf<() -> Unit>()

    val raspberryApi: RaspberryApi
        get() {
            val gson = GsonBuilder().setLenient().create()

            val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.RASPBERRY_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

            return retrofit.create(RaspberryApi::class.java)
        }

    fun waitForFirestoreReady(onReady: () -> Unit  ) {
        if (firestoreReady) {
            onReady()
        } else {
            firestoreWaiters.add(onReady)
        }
    }

    fun getDevices(listener: (MutableList<IotDevice>) -> Unit) {
        homeStorage?.getSmartHome(SmartHomeListener { listener(it.devices) })
    }

    fun setupFirestore(userId: String) {
        val references = FirestoreHomesReferencesStorage(userId)
        references.getHomesReferences(HomesReferencesListener { setupHomeStorage(it.homes) })

        homesReferences = references
    }

    private fun setupHomeStorage(homeIds: List<String>) {
        if (homeIds.isNullOrEmpty()) {
            if (BuildConfig.DEBUG) Log.d(TAG, "no home ids for current user")
            return
        }

        // todo user should choose one home. For now we just choose the first one
        homeStorage = FirestoreSmartHomeStorage(homeIds[0])

        firestoreReady = true
        firestoreWaiters.forEach { it() }
    }
}