package smarthome.library.datalibrary.store

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import smarthome.library.datalibrary.constants.defFailureListener
import smarthome.library.datalibrary.constants.defSuccessListener
import smarthome.library.datalibrary.model.InstanceToken

interface InstanceTokenStorage {

    fun saveToken(
        userId: String,
        token: String,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun getToken(
        userId: String,
        successListener: OnSuccessListener<String>,
        failureListener: OnFailureListener = defFailureListener
    )

    fun removeToken(
        userId: String,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun observeTokenChanges(observer: (List<InstanceToken>) -> Unit)
}