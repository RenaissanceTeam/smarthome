package smarthome.library.datalibrary.constants

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

// database
const val SMART_HOME_REF = "smart_home"
const val TAG = "DataLibrary"

const val LINKED_ACCOUNTS_REF = "linked_accounts"

const val RP_INFO = "rp_info"
const val RP_IP_REF = "ip"
const val RP_PORT_REF = "port"

const val FIREBASE_READ_VALUE_ERROR = "Failed to read value"
const val DEVICES_FIELD_KEY = "devices"

// default listeners for requests
val defSuccessListener: OnSuccessListener<Void> = OnSuccessListener { Log.d(TAG, "data updated successfully") }
val defFailureListener: OnFailureListener = OnFailureListener { e -> Log.w(TAG, "firestore error", e) }
