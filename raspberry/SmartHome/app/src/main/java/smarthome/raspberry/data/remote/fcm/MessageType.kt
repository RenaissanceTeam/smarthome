package smarthome.raspberry.data.remote.fcm

enum class MessageType {
    NOTIFICATION, DATA;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}