package smarthome.client.data.api.scripts

import io.reactivex.Observable
import smarthome.client.entity.script.Script
import smarthome.client.entity.script.ScriptInfo
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.BlockId
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyId

interface SetupScriptRepo {
    fun setScript(script: Script)
    fun addBlock(block: Block): Block
    fun getBlocks(): List<Block>
    fun replaceBlock(block: Block): Block
    fun removeBlock(blockId: BlockId)
    fun addDependency(dependency: Dependency)
    fun updateDependency(dependency: Dependency)
    fun getDependency(dependencyId: DependencyId): Dependency?
    fun observeDependencies(): Observable<List<Dependency>>
    fun observeBlocks(): Observable<List<Block>>
    fun removeDependency(dependencyId: DependencyId)
    fun getBlock(blockId: BlockId): Block?
    fun getScript(): Script?
    fun reset()
    fun setScriptInfo(info: ScriptInfo)
    fun observeScript(): Observable<Script>
}