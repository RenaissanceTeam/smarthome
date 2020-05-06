package smarthome.client.domain.api.scripts.blocks.location

import smarthome.client.entity.script.dependency.condition.Condition

data class HomeGeofenceCondition(
        override val id: String,
        val inside: Boolean = false
): Condition()