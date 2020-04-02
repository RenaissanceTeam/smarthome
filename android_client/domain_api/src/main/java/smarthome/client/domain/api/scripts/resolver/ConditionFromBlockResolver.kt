package smarthome.client.domain.api.scripts.resolver

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.condition.Condition

interface ConditionFromBlockResolver: Resolver<Block, List<Condition>>