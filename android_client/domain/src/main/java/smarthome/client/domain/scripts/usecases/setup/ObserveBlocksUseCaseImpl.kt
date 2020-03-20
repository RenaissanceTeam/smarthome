package smarthome.client.domain.scripts.usecases.setup

import io.reactivex.Observable
import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.ObserveBlocksUseCase
import smarthome.client.entity.script.block.Block

class ObserveBlocksUseCaseImpl(
    private val repo: SetupScriptRepo
) : ObserveBlocksUseCase {
    override fun execute(): Observable<List<Block>> {
        return repo.observeBlocks()
    }
}