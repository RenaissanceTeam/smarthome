package smarthome.raspberry.utils.fcm

enum class MessageType {
    NOTIFICATION, DATA;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}