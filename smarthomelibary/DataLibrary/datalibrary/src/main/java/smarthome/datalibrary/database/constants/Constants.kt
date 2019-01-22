package smarthome.datalibrary.database.constants

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

object Constants {

    // database
    const val SMART_HOME_REF = "smart_home"

    const val LINKED_ACCS_REF = "linked_accounts"

    const val RP_INFO = "rp_info"
    const val RP_IP_REF = "ip"
    const val RP_PORT_REF = "port"

    const val FIREBASE_READ_VALUE_ERROR = "Failed to read value"

    // default listeners for requests
    val defSuccessListener: OnSuccessListener<Void> = OnSuccessListener { Log.d(javaClass.name, "data updated successfully") }
    val defFailureListener: OnFailureListener = OnFailureListener { e -> Log.w(javaClass.name, "firestore error", e) }
}