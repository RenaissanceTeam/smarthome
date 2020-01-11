package smarthome.client.presentation.controllers.controllerdetail.statechanger

enum class StateChangerType {
    ONOFF,
    LEXEME_ONOFF,
    SIMPLE_WRITE,
    DIMMER,
    RGB,
    TEXT_READ_WRITE,
    DIGITS_DECIMAL_READ_WRITE,
    ONLY_READ // default, assume that each controller can be read
}