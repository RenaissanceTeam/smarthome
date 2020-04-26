package smarthome.client.domain.api.scripts.resolver

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.action.Action

interface ActionFromBlockResolver: Resolver<Block, List<Action>>