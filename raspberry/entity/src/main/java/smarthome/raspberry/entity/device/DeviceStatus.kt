package smarthome.raspberry.entity.device

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import smarthome.raspberry.entity.ID_NOT_DEFINED
import javax.persistence.*

@Entity
data class DeviceStatus(
        @Id @GeneratedValue
        val id: Long = ID_NOT_DEFINED,
        @OneToOne
        @OnDelete(action = OnDeleteAction.CASCADE)
        val device: Device,
        val status: String
)
