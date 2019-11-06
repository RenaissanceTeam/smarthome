package smarthome.raspberry.notification.data.command

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import smarthome.raspberry.notification.data.FcmSenderApi
import smarthome.raspberry.notification_api.domain.Message

class SendFcmImplCommandImpl() : SendFcmCommand{
    private val api: FcmSenderApi = Retrofit.Builder()
        .baseUrl("https://us-central1-smarthome-d2238.cloudfunctions.net/")
        .build()
        .create(FcmSenderApi::class.java)
    
    override suspend fun send(message: Message) {
        withContext(Dispatchers.IO) {
            TODO()
//            api.send(messageBody.toRequestBody()).execute()
        }
    }
}