package smarthome.raspberry.notification.domain

enum class NotificationType {
    ALERT, DATA;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}