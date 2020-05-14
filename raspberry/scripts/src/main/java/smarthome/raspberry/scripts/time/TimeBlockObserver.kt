package smarthome.raspberry.scripts.time

import io.reactivex.Observable
import org.joda.time.LocalTime
import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.domain.BlockObserver
import smarthome.raspberry.scripts.api.domain.usecase.GetBlockConditionDependenciesUseCase
import smarthome.raspberry.scripts.api.time.EachDayStateValid
import smarthome.raspberry.scripts.api.time.TimeStateValid
import smarthome.raspberry.scripts.api.time.TimerStateValid
import smarthome.raspberry.scripts.api.time.WeekdayStateValid
import smarthome.raspberry.scripts.api.time.conditions.EachDayCondition
import smarthome.raspberry.scripts.api.time.conditions.TimerCondition
import smarthome.raspberry.scripts.api.time.conditions.WeekdaysCondition
import java.util.*
import java.util.concurrent.TimeUnit

private data class Period(
        val delay: Long,
        val amount: Long,
        val unit: TimeUnit
)

private val LocalTime.millis get() = this.toDateTimeToday().millis

@Component
class TimeBlockObserver(
        private val getBlockConditionDependenciesUseCase: GetBlockConditionDependenciesUseCase
) : BlockObserver<TimeStateValid> {
    override fun execute(blockId: String): Observable<Optional<TimeStateValid>> {
        return getBlockConditionDependenciesUseCase.execute(blockId)
                .flatMap { it.conditions }
                .map { condition ->
                    when (condition) {
                        is TimerCondition -> scheduleTimer(condition)
                        is EachDayCondition -> scheduleEachDay(condition)
                        is WeekdaysCondition -> scheduleWeekdays(condition)
                        else -> throw IllegalArgumentException("Unknown condition for time block: $condition")
                    }

                }
                .let { Observable.merge(it) }
                .map { Optional.of(it) }
                .startWith(Observable.just(Optional.empty()))
    }

    private fun scheduleWeekdays(condition: WeekdaysCondition): Observable<WeekdayStateValid>? {
        return Observable.merge(
                condition.days.map {
                    val firstEmit = getStartOfWeek() + it * condition.time.millis
                    schedulePeriod(
                            Period(calculateDelay(Date(firstEmit)), 7, TimeUnit.DAYS)
                    ).filter { it > System.currentTimeMillis() }
                })
                .map { WeekdayStateValid(condition) }
    }

    private fun scheduleEachDay(condition: EachDayCondition): Observable<EachDayStateValid>? {
        return schedulePeriod(
                Period(calculateDelay(Date(condition.time.millis)), 1, TimeUnit.DAYS)
        ).map { EachDayStateValid(condition) }
    }

    private fun scheduleTimer(condition: TimerCondition): Observable<TimerStateValid>? {
        return schedulePeriod(
                Period(condition.timer.toLong(), condition.timer.toLong(), TimeUnit.SECONDS)
        ).map { TimerStateValid(condition) }
    }

    private fun getStartOfWeek(): Long {
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
}