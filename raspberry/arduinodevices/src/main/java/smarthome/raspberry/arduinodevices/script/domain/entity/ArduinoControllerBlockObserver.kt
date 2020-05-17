package smarthome.raspberry.arduinodevices.script.domain.entity

import io.reactivex.Observable
import org.springframework.stereotype.Component
import smarthome.raspberry.controllers.api.domain.ObserveControllerStatesUseCase
import smarthome.raspberry.scripts.api.controllers.ControllerConditionState
import smarthome.raspberry.scripts.api.domain.BlockObserver
import smarthome.raspberry.scripts.api.domain.usecase.GetBlockByIdUseCase
import java.util.*

@Component
class ArduinoControllerBlockObserver(
        private val getBlockByIdUseCase: GetBlockByIdUseCase,
        private val observeControllerStatesUseCase: ObserveControllerStatesUseCase
) : BlockObserver<ControllerConditionState> {
    override fun execute(blockId: String): Observable<Optional<ControllerConditionState>> {
        val block = getBlockByIdUseCase.execute(blockId)
        require(block is ArduinoControllerBlock)

        return observeControllerStatesUseCase.execute(block.controller.id)
                .map {
                    it.map {
                        ControllerConditionState(
                                block.controller.copy(state = it)
                        )
                    }
                }
    }
}
