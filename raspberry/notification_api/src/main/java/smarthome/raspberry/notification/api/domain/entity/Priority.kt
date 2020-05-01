package smarthome.raspberry.notification.api.domain.entity

enum class Priority {
    NORMAL, HIGH;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}