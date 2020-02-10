package smarthome.client.domain.scripts.usecases

import io.reactivex.Observable
import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.usecases.ObserveBlocksUseCase
import smarthome.client.entity.script.block.Block

class ObserveBlocksUseCaseImpl(
    private val repo: ScriptGraphRepo
) : ObserveBlocksUseCase {
    override fun execute(scriptId: Long): Observable<List<Block>> {
        return repo.observeBlocks(scriptId)
    }
}