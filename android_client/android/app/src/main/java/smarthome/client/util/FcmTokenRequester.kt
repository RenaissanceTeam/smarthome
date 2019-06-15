package smarthome.client.util

import android.content.Context
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import smarthome.client.App
import smarthome.client.BuildConfig
import smarthome.client.domain.usecases.CloudMessageUseCase
import java.lang.RuntimeException
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FcmTokenRequester(private val messageUseCase: CloudMessageUseCase) {

    private val TAG = "FcmTokenRequester"

    suspend fun initFcmToken() {
        if (messageUseCase.noSavedToken()) saveToken()
    }

    private suspend fun saveToken() {
        try {
            val token = requestToken()
            if (BuildConfig.DEBUG) Log.d(TAG, "received token after explicit request=$token")
            messageUseCase.onNewToken(token)
        } catch (e: Throwable) {
            if (BuildConfig.DEBUG) Log.w(TAG, "can't request token: ", e)
        }
    }

    private suspend fun requestToken(): String {
        return suspendCoroutine { c ->
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.token?.let { c.resumeWith(success(it)) }
                            ?: c.resumeWithException(RuntimeException("received null instanceId token"))
                } else {
                    val exception: Throwable = task.exception as? Throwable
                            ?: RuntimeException("instanceId task was not successfull")
                    c.resumeWithException(exception)
                }
            }
        }
    }
}