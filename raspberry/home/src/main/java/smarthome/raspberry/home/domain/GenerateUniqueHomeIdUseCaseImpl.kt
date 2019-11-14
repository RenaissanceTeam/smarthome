package smarthome.raspberry.home.domain

import smarthome.raspberry.home.api.domain.GenerateUniqueHomeIdUseCase
import smarthome.raspberry.home.data.HomeRepository
import java.util.*
import kotlin.random.Random

class GenerateUniqueHomeIdUseCaseImpl(
        private val repository: HomeRepository
) : GenerateUniqueHomeIdUseCase {
    companion object {
        private const val HOME_ID_PREFIX = "home_id"
    }

    override suspend fun execute(): String {
        var homeId: String
        do {
            homeId = generateHomeId()
            val isUnique = repository.isHomeIdUnique(homeId)
        } while (!isUnique)
        return homeId
    }

    private fun generateHomeId(): String {
        val currentTime = Date().time
        val randomPart = Random.nextInt(0, 9999).toString()

        return "$HOME_ID_PREFIX$currentTime$randomPart"
    }
}