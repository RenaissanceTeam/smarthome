package smarthome.library.common

import smarthome.library.common.message.Message

interface MessageQueue {
    suspend fun postMessage(message: Message)
    suspend fun removeMessage(message: Message)
    fun subscribe(listener: MessageListener)
}