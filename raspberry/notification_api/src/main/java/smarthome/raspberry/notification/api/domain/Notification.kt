package smarthome.raspberry.notification.api.domain

data class Notification(
    val title: String,
    val body: String,
    val tokens: Array<String>,
    val type: String,
    val priority: String)

