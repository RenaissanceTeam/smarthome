package smarthome.datalibrary.database.store.firebase

import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import ru.smarthome.library.SmartHome
import smarthome.datalibrary.database.constants.Constants.FIREBASE_READ_VALUE_ERROR
import smarthome.datalibrary.database.constants.Constants.SMART_HOME_REF
import smarthome.datalibrary.database.store.SmartHomeStorage
import smarthome.datalibrary.database.store.listeners.SmartHomeListener

class FirebaseSmartHomeStorage(
    private val uid: String,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : SmartHomeStorage {

    private val ref: DocumentReference = db.collection(uid).document(SMART_HOME_REF)

    override fun postSmartHome(smartHome: SmartHome) {
        ref.set(smartHome)
    }

    override fun postSmartHome(smartHome: SmartHome, listener: OnSuccessListener<Void>) {
        ref.set(smartHome)
            .addOnSuccessListener(listener)
    }

    override fun getSmartHome(listener: SmartHomeListener) {
        ref.get()
            .addOnSuccessListener { res ->
                res.toObject(SmartHome::class.java)?.let { listener.onSmartHomeReceived(it) }
            }
            .addOnFailureListener { exception -> Log.d(javaClass.name, FIREBASE_READ_VALUE_ERROR, exception) }
    }

    companion object {

        private var instance: FirebaseSmartHomeStorage? = null


        fun getInstance(): FirebaseSmartHomeStorage? {
            if (instance == null)
                instance = instantiate()

            return instance
        }

        private fun instantiate(): FirebaseSmartHomeStorage? {
            val auth = FirebaseAuth.getInstance()

            return if (auth.currentUser == null)
                null
            else
                FirebaseSmartHomeStorage(auth.currentUser!!.uid)
        }
    }
}