package smarthome.library.datalibrary.store

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import smarthome.library.datalibrary.constants.defFailureListener
import smarthome.library.datalibrary.constants.defSuccessListener
import smarthome.library.datalibrary.model.InstanceTokenWrapper

interface InstanceTokenStorage {

    fun saveToken(
        userId: String,
        token: String,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun getTokens(
        userId: String,
        successListener: OnSuccessListener<List<String>>,
        failureListener: OnFailureListener = defFailureListener
    )

    fun getUsersToTokens(
        successListener: OnSuccessListener < List< Pair<String, InstanceTokenWrapper> > >,
        failureListener: OnFailureListener = defFailureListener
    )

    fun removeToken(
        userId: String,
        token: String,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun observeTokenChanges(observer: (List< Pair<String, InstanceTokenWrapper> >) -> Unit)
}