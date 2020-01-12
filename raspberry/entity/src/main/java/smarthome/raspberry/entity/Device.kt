package smarthome.raspberry.entity

import javax.persistence.*

@Entity
class Device(
    @Id @GeneratedValue
    val id: Long = 0,
    val serialName: String,
    val name: String = "",
    val description: String = "",
    val type: String,
    
    @OneToMany(mappedBy = "device", cascade = [CascadeType.ALL])
    @OrderBy("id ASC")
    val controllers: List<Controller>
)
