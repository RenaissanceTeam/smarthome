package smarthome.client.fragments.controllerdetail.statechanger

enum class StateChangerType {
    ONOFF,
    LEXEME_ONOFF,
    SIMPLE_WRITE,
    DIMMER,
    GATEWAY_DIMMER,
    GATEWAY_RGB,
    TEXT_READ_WRITE,
    ONLY_READ // default, assume that each controller can be read
}