package smarthome.raspberry.domain.usecases

import smarthome.raspberry.domain.HomeRepository

class HomeUseCase(private val repository: HomeRepository) {
    /**
     * First use case, after that home should be ready to other use cases
     *
     * Given that the user is authorized:
     * 1) Setup source of user interaction - some channel to receive from user requests
     * 2) Setup input source of devices interaction - ability for the device to start communication
     * to home infrastructure
     *
     */
    suspend fun start() {
        repository.setupUserInteraction()
        repository.setupDevicesInteraction()
    }
}