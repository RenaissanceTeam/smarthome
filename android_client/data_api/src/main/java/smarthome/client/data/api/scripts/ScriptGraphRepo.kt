package smarthome.client.data.api.scripts

import smarthome.client.entity.script.Block
import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Dependency

interface ScriptGraphRepo {
    fun add(scriptId: Long, block: Block)
    fun getBlocks(scriptId: Long): List<Block>
    fun replaceBlock(block: Block): Block
    fun remove(scriptId: Long, blockId: BlockId)
    fun addDependency(dependency: Dependency)
}