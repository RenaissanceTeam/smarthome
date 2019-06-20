package smarthome.raspberry.domain

interface HomeRepository {

    suspend fun setupUserInteraction()

    suspend fun setupDevicesInteraction()

}