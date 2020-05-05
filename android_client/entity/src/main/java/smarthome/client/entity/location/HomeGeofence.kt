package smarthome.client.entity.location

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HomeGeofence(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val lat: Double = 0.0,
        val lon: Double = 0.0,
        val radius: Int = 0
)