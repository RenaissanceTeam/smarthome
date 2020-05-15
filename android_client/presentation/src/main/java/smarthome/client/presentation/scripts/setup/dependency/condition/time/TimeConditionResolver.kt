package smarthome.client.presentation.scripts.setup.dependency.condition.time

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.domain.api.scripts.blocks.time.EachDayCondition
import smarthome.client.domain.api.scripts.blocks.time.TimerCondition
import smarthome.client.domain.api.scripts.blocks.time.WeekdaysCondition
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyConditionUseCase
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

class TimeConditionResolver(
        private val changeCondition: ChangeSetupDependencyConditionUseCase
) : ConditionModelResolver {
    override fun canResolve(item: Condition): Boolean {
        return when (item) {
            is EachDayCondition,
            is TimerCondition,
            is WeekdaysCondition -> true
            else -> false
        }
    }

    override fun resolve(item: Condition): EpoxyModel<*> {
        return when (item) {
            is EachDayCondition -> constructEachDayViewModel(item)
            is TimerCondition -> constructTimerViewModel(item)
            is WeekdaysCondition -> constructWeekdaysViewModel(item)
            else -> throw IllegalArgumentException("Can't create model for $item")
        }
    }

    private fun constructEachDayViewModel(eachDay: EachDayCondition): EpoxyModel<EachDayConditionView> {
        return EachDayConditionViewModel_().apply {
            id(eachDay.id.hashCode())

            time(eachDay.time)
            onChangeTime { newTime ->
                changeCondition.execute(eachDay.id) {
                    (it as? EachDayCondition)?.copy(time = newTime) ?: it
                }
            }
        }
    }

    private fun constructTimerViewModel(timer: TimerCondition): EpoxyModel<TimerConditionView> {
        return TimerConditionViewModel_().apply {
            id(timer.id.hashCode())

            timer(timer.timer)
            onChangeTimer { newTimer ->
                changeCondition.execute(timer.id) {
                    (it as? TimerCondition)?.copy(timer = newTimer) ?: it
                }
            }
        }
    }

    private fun constructWeekdaysViewModel(weekdays: WeekdaysCondition) =
            WeekdaysConditionViewModel_().apply {
                id(weekdays.id.hashCode())

                days(weekdays.days)
                onChangeSelected { newSelected ->
                    changeCondition.execute(weekdays.id) {
                        (it as? WeekdaysCondition)?.copy(days = newSelected) ?: it
                    }
                }

                time(weekdays.time)
                onChangeTime { newTime ->
                    changeCondition.execute(weekdays.id) {
                        (it as? WeekdaysCondition)?.copy(time = newTime) ?: it
                    }
                }
            }
}