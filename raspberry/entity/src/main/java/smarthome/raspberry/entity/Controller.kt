package smarthome.raspberry.entity

import javax.persistence.*

@Entity
data class Controller(
    @ManyToOne
    val device: Device,
    @Id @GeneratedValue
    val id: Long = 0,
    val type: String,
    val name: String,
    val state: String
)