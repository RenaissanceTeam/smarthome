package smarthome.client.presentation.scripts.setup.dependency.mock

import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition

class MockCondition(override val id: String?, val data: String = "") : Condition()
class MockAction(override val id: String?) : Action()