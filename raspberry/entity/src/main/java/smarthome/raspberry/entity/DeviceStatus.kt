package smarthome.raspberry.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
data class DeviceStatus(
        @Id @GeneratedValue
        val id: Long = 0,
        @OneToOne
        @OnDelete(action = OnDeleteAction.CASCADE)
        val device: Device,
        val status: String
)
