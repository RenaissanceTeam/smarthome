package smarthome.client.viewpager.scripts

class Script(val name: String, val condition: Condition, val action: Action)

abstract class Condition {
    abstract fun evaluate(): Boolean
}

class MockCondition: Condition() {
    override fun evaluate(): Boolean = true
    override fun toString() = "'GarageMovementSensor' is triggered"
}

abstract class Action {
    abstract fun action()
}

class MockAction: Action() {
    override fun action() {}
    override fun toString() = "Turn on 'GarageLight'"
}