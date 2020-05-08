package smarthome.raspberry.scripts.time

import io.reactivex.Observable
import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.scripts.api.domain.BlockObserver
import smarthome.raspberry.scripts.api.domain.usecase.GetBlockConditionDependenciesUseCase
import smarthome.raspberry.scripts.api.time.EachDayCondition
import smarthome.raspberry.scripts.api.time.TimerCondition
import smarthome.raspberry.scripts.api.time.data.TimeBlockStateRepository
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class TimeBlockObserver(
        private val getBlockConditionDependenciesUseCase: GetBlockConditionDependenciesUseCase,
        private val timeBlockStateRepository: TimeBlockStateRepository

) : BlockObserver {
    override fun execute(blockId: String): Observable<Optional<Block>> {
        getBlockConditionDependenciesUseCase.execute(blockId)
                .flatMap { it.conditions }
                .map {
                    when (it) {
                        is TimerCondition -> Observable.interval(
                                it.timer.toLong(),
                                it.timer.toLong(),
                                TimeUnit.SECONDS
                        )
                        is EachDayCondition ->
                    }
                }
    }
}