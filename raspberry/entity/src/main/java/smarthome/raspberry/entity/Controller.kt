package smarthome.raspberry.entity

import javax.persistence.*

@Entity
data class Controller(
    @Id @GeneratedValue
    val id: Long = 0,
    @ManyToOne
    val device: Device,
    val type: String,
    val name: String,
    val state: String
)