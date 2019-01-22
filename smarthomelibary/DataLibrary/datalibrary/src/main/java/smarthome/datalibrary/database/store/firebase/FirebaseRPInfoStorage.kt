package smarthome.datalibrary.database.store.firebase

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import smarthome.datalibrary.database.constants.Constants.FIREBASE_READ_VALUE_ERROR
import smarthome.datalibrary.database.constants.Constants.RP_INFO
import smarthome.datalibrary.database.constants.Constants.RP_IP_REF
import smarthome.datalibrary.database.constants.Constants.RP_PORT_REF
import smarthome.datalibrary.database.store.RPInfoStorage
import smarthome.datalibrary.database.store.listeners.RPInfoListener

class FirebaseRPInfoStorage private constructor(
    private val uid: String,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : RPInfoStorage {

    private val ref: DocumentReference = db.collection(uid).document(RP_INFO)

    private var rpInfoMap = HashMap<String, Any>()

    override fun postRaspberryIp(
        ip: String,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        rpInfoMap[RP_IP_REF] = ip
        ref.set(rpInfoMap)
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun postRaspberryPort(
        port: String,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        rpInfoMap[RP_PORT_REF] = port
        ref.set(rpInfoMap)
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun getRaspberryInfo(listener: RPInfoListener) {
        ref.get()
            .addOnSuccessListener { res ->
                run {
                    if (res != null) {
                        val responseInfoMap = res.data
                        listener.onRaspberryIpReceived(responseInfoMap?.get(RP_IP_REF) as String)
                        listener.onRaspberryPortReceived(responseInfoMap[RP_PORT_REF] as String)
                    }
                }
            }
            .addOnFailureListener { exception -> Log.d(javaClass.name, FIREBASE_READ_VALUE_ERROR, exception) }
    }

    companion object {

        private var instance: FirebaseRPInfoStorage? = null


        fun getInstance(): FirebaseRPInfoStorage? {
            if (instance == null)
                instance = instantiate()

            return instance
        }

        private fun instantiate(): FirebaseRPInfoStorage? {
            val auth = FirebaseAuth.getInstance()

            auth.currentUser?.let { return FirebaseRPInfoStorage(it.uid) } ?: return null
        }
    }
}
