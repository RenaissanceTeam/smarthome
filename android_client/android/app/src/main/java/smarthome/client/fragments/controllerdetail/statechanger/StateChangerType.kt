package smarthome.client.fragments.controllerdetail.statechanger

enum class StateChangerType {
    ONOFF,
    ONLY_READ // default, assume that each controller can be read
}