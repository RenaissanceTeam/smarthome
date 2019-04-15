package smarthome.client.scripts.actions

import smarthome.client.scripts.actions.Action

class MockAction : Action() {
    override fun action() {}
    override fun toString() = "Turn on 'GarageLight'"
}