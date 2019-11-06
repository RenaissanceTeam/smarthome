package smarthome.raspberry.notification.domain

enum class MessageType {
    NOTIFICATION, DATA;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}