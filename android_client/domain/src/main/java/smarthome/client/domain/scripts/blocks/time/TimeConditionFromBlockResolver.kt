package smarthome.client.domain.scripts.blocks.time

import smarthome.client.domain.api.scripts.blocks.time.EachDayCondition
import smarthome.client.domain.api.scripts.blocks.time.TimerCondition
import smarthome.client.domain.api.scripts.blocks.time.WeekdaysCondition
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.TimeBlock
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.util.generateId

class TimeConditionFromBlockResolver : ConditionFromBlockResolver {
    override fun canResolve(item: Block): Boolean {
        return item is TimeBlock
    }

    override fun resolve(item: Block): List<Condition> {
        return listOf(
                TimerCondition(generateId()),
                WeekdaysCondition(generateId()),
                EachDayCondition(generateId())
        )
    }
}