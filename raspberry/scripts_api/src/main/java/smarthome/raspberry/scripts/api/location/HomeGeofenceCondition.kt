package smarthome.raspberry.scripts.api.location

import smarthome.raspberry.entity.script.Condition
import javax.persistence.Entity

@Entity
data class HomeGeofenceCondition(
        override val id: String,
        val inside: Boolean
) : Condition(id)