package smarthome.raspberry.entity.script

import javax.persistence.Embeddable

@Embeddable
data class Position(
        val x: Int,
        val y: Int
)