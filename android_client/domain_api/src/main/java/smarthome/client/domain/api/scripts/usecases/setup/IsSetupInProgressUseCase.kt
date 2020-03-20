package smarthome.client.domain.api.scripts.usecases.setup

interface IsSetupInProgressUseCase {
    fun execute(id: Long): Boolean
}