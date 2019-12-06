package smarthome.library.datalibrary.constants

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

internal const val TAG = "DataLibrary"

// database
internal const val ACCOUNTS_NODE = "accounts"
internal const val HOMES_NODE = "homes"

internal const val ACCOUNT_HOMES_ARRAY_REF = "homes"
internal const val HOME_DEVICES_NODE = "devices"
internal const val HOME_USERS_NODE = "users"
internal const val MESSAGES_NODE = "message_queue"
internal const val PENDING_DEVICES_NODE = "pending_devices"

internal const val FIREBASE_READ_VALUE_ERROR = "Failed to read value"


// default listeners for requests
internal val defSuccessListener: OnSuccessListener<Void> = OnSuccessListener { Log.d(TAG, "data updated successfully") }
internal val defFailureListener: OnFailureListener = OnFailureListener { e -> Log.w(TAG, "firestore error", e) }
