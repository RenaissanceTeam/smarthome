package smarthome.client.util

import com.google.firebase.iid.FirebaseInstanceId
import smarthome.client.domain.domain.usecases.CloudMessageUseCase
import kotlin.Result.Companion.success
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FcmTokenRequester(private val messageUseCase: smarthome.client.domain.domain.usecases.CloudMessageUseCase) {

    suspend fun initFcmToken() {
        if (messageUseCase.noSavedToken()) saveToken()
    }

    private suspend fun saveToken() {
        try {
            val token = requestToken()
            messageUseCase.onNewToken(token)
        } catch (e: Throwable) {
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