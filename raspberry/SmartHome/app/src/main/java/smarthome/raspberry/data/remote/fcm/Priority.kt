package smarthome.raspberry.data.remote.fcm

enum class Priority {
    NORMAL, HIGH;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}