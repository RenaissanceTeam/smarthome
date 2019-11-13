package smarthome.raspberry.notification.api.domain

enum class Priority {
    NORMAL, HIGH;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}