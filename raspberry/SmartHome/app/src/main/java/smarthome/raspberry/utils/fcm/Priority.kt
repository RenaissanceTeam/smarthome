package smarthome.raspberry.utils.fcm

enum class Priority {
    NORMAL, HIGH;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}