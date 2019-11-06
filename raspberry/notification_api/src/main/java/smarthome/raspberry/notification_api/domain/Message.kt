package smarthome.raspberry.notification_api.domain

data class Message(
    val title: String,
    val body: String,
    val tokens: Array<String>,
    val type: String,
    val priority: String)

