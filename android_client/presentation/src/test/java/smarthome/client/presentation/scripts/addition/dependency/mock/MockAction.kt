package smarthome.client.presentation.scripts.addition.dependency.mock

import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.action.Action

data class MockAction(override val dependencyId: DependencyId): Action