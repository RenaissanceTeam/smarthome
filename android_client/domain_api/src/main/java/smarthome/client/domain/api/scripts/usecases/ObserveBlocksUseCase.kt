package smarthome.client.domain.api.scripts.usecases

import io.reactivex.Observable
import smarthome.client.entity.script.block.Block

interface ObserveBlocksUseCase {
    fun execute(scriptId: Long): Observable<List<Block>>
}