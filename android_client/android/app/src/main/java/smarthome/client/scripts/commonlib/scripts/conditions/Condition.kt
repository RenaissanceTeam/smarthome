package smarthome.client.scripts.commonlib.scripts.conditions

abstract class Condition {
    abstract fun evaluate(): Boolean
}
