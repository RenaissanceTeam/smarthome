package smarthome.raspberry.scripts.domain

import io.reactivex.Observable
import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.scripts.api.domain.ObserveBlockStatesUseCase

@Component
class ObserveBlockStatesUseCaseImpl(

) : ObserveBlockStatesUseCase {
    override fun execute(blockId: String): Observable<Block> {
        TODO()
    }
}