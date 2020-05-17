package smarthome.raspberry.scripts.api.time

import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.scripts.api.domain.ConditionState
import smarthome.raspberry.scripts.api.time.conditions.EachDayCondition
import smarthome.raspberry.scripts.api.time.conditions.TimerCondition
import smarthome.raspberry.scripts.api.time.conditions.WeekdaysCondition

sealed class TimeStateValid(val condition: Condition): ConditionState()

class EachDayStateValid(condition: EachDayCondition): TimeStateValid(condition)
class TimerStateValid(condition: TimerCondition): TimeStateValid(condition)
class WeekdayStateValid(condition: WeekdaysCondition): TimeStateValid(condition)