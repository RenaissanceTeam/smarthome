package smarthome.client.data.api.scripts

import io.reactivex.Observable
import smarthome.client.entity.script.Script
import smarthome.client.entity.script.ScriptInfo
import smarthome.client.entity.script.block.Block

import smarthome.client.entity.script.dependency.Dependency
interface SetupScriptRepo {
    fun setScript(script: Script)
    fun addBlock(block: Block): Block
    fun getBlocks(): List<Block>
    fun replaceBlock(block: Block): Block
    fun removeBlock(blockId: String)
    fun addDependency(dependency: Dependency)
    fun updateDependency(dependency: Dependency)
    fun getDependency(dependencyId: String): Dependency?
    fun observeDependencies(): Observable<List<Dependency>>
    fun observeBlocks(): Observable<List<Block>>
    fun removeDependency(dependencyId: String)
    fun getBlock(blockId: String): Block?
    fun getScript(): Script?
    fun reset()
    fun setScriptInfo(info: ScriptInfo)
    fun observeScript(): Observable<Script>
}