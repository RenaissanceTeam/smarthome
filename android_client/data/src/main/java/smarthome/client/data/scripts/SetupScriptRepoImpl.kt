package smarthome.client.data.scripts

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.entity.script.Script
import smarthome.client.entity.script.ScriptInfo
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.util.findAndModify
import smarthome.client.util.onNextModified
import smarthome.client.util.withRemoved

class SetupScriptRepoImpl : SetupScriptRepo {
    private var script = BehaviorSubject.create<Script>()
    
    override fun reset() {
        script.onComplete()
        script = BehaviorSubject.create()
    }
    
    override fun addBlock(block: Block): Block {
        script.onNextModified { it?.copy(blocks = it.blocks + block) }
        return block
    }
    
    override fun getBlocks(): List<Block> {
        return script.value?.blocks.orEmpty()
    }
    
    override fun getBlock(blockId: String): Block? {
        return getBlocks().find { it.id == blockId }
    }
    
    override fun getScript(): Script? {
        return script.value
    }
    
    override fun setScript(script: Script) {
        this.script.onNext(script)
    }
    
    override fun setScriptInfo(info: ScriptInfo) {
        script.onNextModified {
            it.copy(
                name = info.name,
                description = info.description
            )
        }
    }
    
    override fun observeScript(): Observable<Script> {
        return script
    }
    
    override fun replaceBlock(block: Block): Block {
        script.onNextModified { current ->
            val replaced = current.blocks.findAndModify({ it.id == block.id }, { block })
            current.copy(blocks = replaced)
        }
        return block
    }
    
    override fun removeBlock(blockId: String) {
        script.onNextModified {
            val removed = it.blocks.withRemoved { it.id == blockId }
            it.copy(blocks = removed)
        }
    }
    
    override fun addDependency(dependency: Dependency) {
        script.onNextModified { it.copy(dependencies = it.dependencies + dependency) }
    }
    
    override fun updateDependency(dependency: Dependency) {
        script.onNextModified { current ->
            val updated = current.dependencies.findAndModify({ it.id == dependency.id }, { dependency })
            current.copy(dependencies = updated)
        }
    }
    
    override fun getDependency(dependencyId: String): Dependency? {
        return script.value?.dependencies.orEmpty().find { it.id == dependencyId }
    }
    
    override fun observeDependencies(): Observable<List<Dependency>> {
        return script.map { it.dependencies }
    }
    
    override fun observeBlocks(): Observable<List<Block>> {
        return script.map { it.blocks }
    }
    
    override fun removeDependency(dependencyId: String) {
        script.onNextModified {
            val removed = it.dependencies.withRemoved { it.id == dependencyId }
            it.copy(dependencies = removed)
        }
    }
}