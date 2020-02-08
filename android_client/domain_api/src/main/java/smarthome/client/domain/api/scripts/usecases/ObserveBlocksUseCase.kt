package smarthome.client.domain.api.scripts.usecases

import io.reactivex.Observable
import smarthome.client.entity.script.Block

interface ObserveBlocksUseCase {
    fun execute(scriptId: Long): Observable<List<Block>>
}