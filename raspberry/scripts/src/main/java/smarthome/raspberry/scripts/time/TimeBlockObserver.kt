package smarthome.raspberry.scripts.time

import io.reactivex.Observable
import org.joda.time.LocalTime
import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.scripts.api.domain.BlockObserver
import smarthome.raspberry.scripts.api.domain.usecase.GetBlockByIdUseCase
import smarthome.raspberry.scripts.api.domain.usecase.GetBlockConditionDependenciesUseCase
import smarthome.raspberry.scripts.api.time.*
import smarthome.raspberry.scripts.api.time.conditions.EachDayCondition
import smarthome.raspberry.scripts.api.time.conditions.TimerCondition
import smarthome.raspberry.scripts.api.time.conditions.WeekdaysCondition
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class TimeBlockObserver(
        private val getBlockConditionDependenciesUseCase: GetBlockConditionDependenciesUseCase,
        private val getBlockByIdUseCase: GetBlockByIdUseCase

) : BlockObserver<TimeStateValid> {
    override fun execute(blockId: String): Observable<Optional<TimeStateValid>> {
        return getBlockConditionDependenciesUseCase.execute(blockId)
                .flatMap { it.conditions }
                .map { condition ->
                    when (condition) {
                        is TimerCondition -> schedulePeriod(
                                Period(condition.timer.toLong(), condition.timer.toLong(), TimeUnit.SECONDS)
                        ).map { TimerStateValid(condition) }
                        is EachDayCondition -> schedulePeriod(
                                Period(calculateDelay(Date(condition.time.millis)), 1, TimeUnit.DAYS)
                        ).map { EachDayStateValid(condition) }
                        is WeekdaysCondition -> {
                            Observable.merge(
                                    condition.days.map {
                                        val firstEmit = getStartOfWeek() + it * condition.time.millis
                                        schedulePeriod(
                                                Period(calculateDelay(Date(firstEmit)), 7, TimeUnit.DAYS)
                                        ).filter { it > System.currentTimeMillis() }
                                    })
                                    .map { WeekdayStateValid(condition) }

                        }
                        else -> throw IllegalArgumentException("Unknown condition for time block: $condition")
                    }

                }
                .let { Observable.merge(it) }
                .map { Optional.of(it) }
                .startWith(Observable.just(Optional.empty()))
    }

    private val LocalTime.millis get() = this.toDateTimeToday().millis

    fun getStartOfWeek(): Long {
        return Calendar.getInstance().apply {
            time = Date(System.currentTimeMillis())
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun calculateDelay(date: Date): Long {
        val currentTime = System.currentTimeMillis()
        return date.time - currentTime
    }

    private fun schedulePeriod(period: Period): Observable<Long> {
        return Observable.interval(period.delay, period.amount, period.unit)
                .map { System.currentTimeMillis() }
    }


    private fun mapTimeToBlockState(blockId: String, time: Long): Optional<Block> {
        return (getBlockByIdUseCase.execute(blockId) as? TimeBlock)
                ?.copy(time = LocalTime(time))
                .let { Optional.ofNullable(it) }
    }

    data class Period(
            val delay: Long,
            val amount: Long,
            val unit: TimeUnit
    )
}