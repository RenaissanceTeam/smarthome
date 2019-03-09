package smarthome.library.datalibrary.constants

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

const val TAG = "DataLibrary"

const val DEFAULT_HOME_ID = "default_home_id"

// database
const val ACCOUNTS_NODE = "accounts"
const val HOMES_NODE = "homes"

const val ACCOUNT_HOMES_ARRAY_REF = "homes"
const val HOME_DEVICES_NODE = "devices"

const val FIREBASE_READ_VALUE_ERROR = "Failed to read value"


// default listeners for requests
val defSuccessListener: OnSuccessListener<Void> = OnSuccessListener { Log.d(TAG, "data updated successfully") }
val defFailureListener: OnFailureListener = OnFailureListener { e -> Log.w(TAG, "firestore error", e) }
