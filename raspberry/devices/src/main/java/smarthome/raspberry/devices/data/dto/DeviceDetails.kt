package smarthome.raspberry.devices.data.dto

import smarthome.raspberry.entity.Controller
import javax.persistence.CascadeType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

data class DeviceDetails(
        val id: Long,
        val serialName: String,
        val name: String,
        val description: String,
        val type: String,
        val controllers: List<GeneralControllerInfo>
)