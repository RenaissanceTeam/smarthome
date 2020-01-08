package smarthome.raspberry.entity

import javax.persistence.*

@Entity
data class Device(
    @Id @GeneratedValue
    val id: Long = 0,
    val serialName: String,
    val name: String = "",
    val description: String = "",
    val type: String,
    
    @OneToMany(mappedBy = "device", cascade = [CascadeType.ALL])
    val controllers: List<Controller>
)

