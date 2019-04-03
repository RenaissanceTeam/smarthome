package smarthome.raspberry.utils.fcm

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.raspberry.BuildConfig.DEBUG
import kotlin.coroutines.suspendCoroutine

class FcmSender(private val context: Context) {
    val api: FcmSenderApi = Retrofit.Builder()
            .baseUrl("https://us-central1-smarthome-d2238.cloudfunctions.net/")
            .build().create(FcmSenderApi::class.java)


    val TAG = "FcmSender"

    suspend fun send(controller: BaseController, device: IotDevice, type: MessageType, priority: Priority,
             tokens: Array<String>) {
        val messageBody =
                MessageComposer.createMessageBody(context, controller, device, type, priority, tokens)
        val response = withContext(Dispatchers.IO) { api.send(messageBody.toRequestBody()).execute() }
        if (DEBUG) Log.d(TAG, "fcm send response: $response")
    }
}