package smarthome.client.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HomeServer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val url: String = "",
    val active: Boolean = false
)
