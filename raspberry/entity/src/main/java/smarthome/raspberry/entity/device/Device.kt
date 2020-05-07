package smarthome.raspberry.entity.device

import smarthome.raspberry.entity.ID_NOT_DEFINED
import smarthome.raspberry.entity.controller.Controller
import javax.persistence.*

@Entity
data class Device(
        @Id @GeneratedValue
        val id: Long = ID_NOT_DEFINED,
        val serial: Int,
        val name: String = "",
        val description: String = "",
        val type: String,

        @OneToMany(mappedBy = "device", cascade = [CascadeType.ALL])
        @OrderBy("id ASC")
        val controllers: List<Controller>
)
