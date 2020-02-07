package smarthome.client.data.scripts

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.BlockId
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.util.findAndModify
import smarthome.client.util.withRemoved

class ScriptGraphRepoImpl : ScriptGraphRepo {
    private val blocks = BehaviorSubject.create<List<Block>>()
    private val dependencies = BehaviorSubject.create<List<Dependency>>()
    
    override fun addBlock(scriptId: Long, block: Block): Block {
        blocks.onNext(blocks.value.orEmpty() + block)
        return block
    }
    
    override fun getBlocks(scriptId: Long): List<Block> {
        return blocks.value.orEmpty()
    }
    
    override fun getBlock(scriptId: Long, blockId: BlockId): Block? {
        return getBlocks(scriptId).find { it.id == blockId }
    }
    
    override fun replaceBlock(scriptId: Long, block: Block): Block {
        blocks.onNext(
            blocks.value
                .orEmpty()
                .findAndModify({ it.id == block.id }, { block })
        )
        return block
    }
    
    override fun removeBlock(scriptId: Long, blockId: BlockId) {
        blocks.onNext(blocks.value.orEmpty().withRemoved { it.id == blockId })
    }
    
    override fun addDependency(scriptId: Long, dependency: Dependency) {
        dependencies.onNext(dependencies.value.orEmpty() + dependency)
    }
    
    override fun getDependency(scriptId: Long, dependencyId: DependencyId): Dependency? {
        return dependencies.value.orEmpty().find { it.id == dependencyId }
    }
    
    override fun observeDependencies(scriptId: Long): Observable<List<Dependency>> {
        return dependencies
    }
    
    override fun observeBlocks(scriptId: Long): Observable<List<Block>> {
        return blocks
    }
    
    override fun removeDependency(scriptId: Long, dependencyId: DependencyId) {
        dependencies.onNext(dependencies.value.orEmpty().withRemoved { it.id == dependencyId })
    }
}