package smarthome.library.datalibrary.store

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import smarthome.library.common.message.Message
import smarthome.library.datalibrary.constants.defFailureListener
import smarthome.library.datalibrary.constants.defSuccessListener
import smarthome.library.datalibrary.store.listeners.MessageListener

interface MessageQueue {

    fun postMessage(message: Message,
                    successListener: OnSuccessListener<Void> = defSuccessListener,
                    failureListener: OnFailureListener = defFailureListener
    )

    fun removeMessage(message: Message,
                      successListener: OnSuccessListener<Void> = defSuccessListener,
                      failureListener: OnFailureListener = defFailureListener)

    fun subscribe(listener: MessageListener)

}