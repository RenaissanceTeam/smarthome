package smarthome.library.datalibrary.store

import smarthome.library.common.message.Message
import smarthome.library.datalibrary.store.listeners.MessageListener

interface MessageQueue {
    suspend fun postMessage(message: Message)
    suspend fun removeMessage(message: Message)
    fun subscribe(listener: MessageListener)
}