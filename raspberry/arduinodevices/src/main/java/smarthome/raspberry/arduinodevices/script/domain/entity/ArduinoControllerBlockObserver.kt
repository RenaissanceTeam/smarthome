package smarthome.raspberry.arduinodevices.script.domain.entity

import io.reactivex.Observable
import org.springframework.stereotype.Component
import smarthome.raspberry.controllers.api.domain.ObserveControllerStatesUseCase
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.scripts.api.domain.BlockObserver
import smarthome.raspberry.scripts.api.domain.GetBlockByIdUseCase
import java.util.*

@Component
class ArduinoControllerBlockObserver(
        private val getBlockByIdUseCase: GetBlockByIdUseCase,
        private val observeControllerStatesUseCase: ObserveControllerStatesUseCase
) : BlockObserver {
    override fun execute(blockId: String): Observable<Optional<Block>> {
        val block = getBlockByIdUseCase.execute(blockId)
        require(block is ArduinoControllerBlock)

        return observeControllerStatesUseCase.execute(block.controller.id)
                .map {
                    it.map {
                        ArduinoControllerBlock(
                                block.id,
                                block.position,
                                block.type,
                                block.controller.copy(state = it)
                        ) as Block
                    }
                }
    }
}
