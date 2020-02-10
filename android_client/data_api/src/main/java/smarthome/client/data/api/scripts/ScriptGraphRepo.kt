package smarthome.client.data.api.scripts

import io.reactivex.Observable
import smarthome.client.entity.script.Block
import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Dependency
import smarthome.client.entity.script.DependencyId

interface ScriptGraphRepo {
    fun addBlock(scriptId: Long, block: Block): Block
    fun getBlocks(scriptId: Long): List<Block>
    fun replaceBlock(scriptId: Long, block: Block): Block
    fun removeBlock(scriptId: Long, blockId: BlockId)
    fun addDependency(scriptId: Long, dependency: Dependency)
    fun observeDependencies(scriptId: Long): Observable<List<Dependency>>
    fun observeBlocks(scriptId: Long): Observable<List<Block>>
    fun removeDependency(scriptId: Long, dependencyId: DependencyId)
}