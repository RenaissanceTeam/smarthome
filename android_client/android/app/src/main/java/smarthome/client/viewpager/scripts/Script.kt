package smarthome.client.viewpager.scripts

class Script(val name: String, val condition: Condition, val action: Action)

abstract class Condition {
    abstract fun evaluate(): Boolean
}

class MockCondition: Condition() {
    override fun evaluate(): Boolean = true
    override fun toString() = "mock condition"
}

abstract class Action {
    abstract fun action()
}

class MockAction: Action() {
    override fun action() {}
    override fun toString() = "mock action"
}