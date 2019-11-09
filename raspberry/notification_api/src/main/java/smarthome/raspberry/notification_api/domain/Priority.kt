package smarthome.raspberry.notification_api.domain

enum class Priority {
    NORMAL, HIGH;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}