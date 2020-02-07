package smarthome.client.entity.script.dependency

import smarthome.client.entity.script.block.BlockId

data class Dependency(
    val id: DependencyId,
    val startBlock: BlockId,
    val endBlock: BlockId
)