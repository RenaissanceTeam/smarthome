package smarthome.client.presentation.scripts.addition.graph

import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Dependency
import smarthome.client.entity.script.DependencyId

data class MockDependency(
    override val id: DependencyId,
    override val startBlock: BlockId,
    override val endBlock: BlockId
) : Dependency