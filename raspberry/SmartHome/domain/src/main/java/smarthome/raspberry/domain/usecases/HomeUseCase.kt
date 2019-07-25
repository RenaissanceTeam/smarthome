package smarthome.raspberry.domain.usecases

import smarthome.raspberry.domain.HomeRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class HomeUseCase(private val repository: HomeRepository) {
    private val HOME_ID_PREFIX = "home_id"

    suspend fun generateUniqueHomeId(): String {
        var homeId: String
        do {
            homeId = generateHomeId()
            val isUnique = repository.isHomeIdUnique(homeId)
        } while (!isUnique)
        return homeId
    }

    private fun generateHomeId(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val currentTime = LocalDateTime.now().format(formatter)

        val randomPart = Random.nextInt(0, 9999).toString()

        return "$HOME_ID_PREFIX$currentTime$randomPart"
    }
}