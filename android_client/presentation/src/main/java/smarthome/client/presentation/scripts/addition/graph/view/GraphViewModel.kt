package smarthome.client.presentation.scripts.addition.graph.view

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import smarthome.client.domain.api.scripts.usecases.setup.*
import smarthome.client.entity.script.block.BlockId
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyState
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.IDLE
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MovingDependency
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BorderStatus
import smarthome.client.presentation.scripts.addition.graph.eventhandler.DependencyEventsHandler
import smarthome.client.presentation.scripts.addition.graph.eventhandler.DragBlockEventsHandler
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.DRAG_CANCEL
import smarthome.client.presentation.scripts.addition.graph.events.drag.DRAG_DROP
import smarthome.client.presentation.scripts.addition.graph.events.drag.GRAPH
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent
import smarthome.client.presentation.scripts.addition.graph.events.navigation.OpenSetupDependency
import smarthome.client.presentation.scripts.addition.graph.mapper.BlockToNewGraphBlockStateMapper
import smarthome.client.presentation.scripts.addition.graph.mapper.DependencyToDependencyStateMapper
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.extensions.triggerRebuild
import smarthome.client.presentation.util.extensions.updateWith
import smarthome.client.util.Position
import smarthome.client.util.findAndModify

class GraphViewModel : KoinViewModel() {
    private val eventBus: GraphEventBus by inject()
    private val observeBlocksUseCase: ObserveBlocksUseCase by inject()
    private val observeDependenciesUseCase: ObserveDependenciesUseCase by inject()
    private val checkIfDependencyPossible: CheckIfDependencyPossibleUseCase by inject()
    private val blockToNewGraphBlockMapper: BlockToNewGraphBlockStateMapper by inject()
    private val dependencyToDependencyStateMapper: DependencyToDependencyStateMapper by inject()
    private val addDependencyUseCase: AddDependencyUseCase by inject()
    private val dragBlockHandler: DragBlockEventsHandler by inject { parametersOf(blocks) }
    private val dependencyEventsHandler: DependencyEventsHandler by inject { parametersOf(movingDependency) }
    private val moveBlockUseCase: MoveBlockUseCase by inject()
    
    val movingDependency = MutableLiveData<MovingDependency>()
    val blocks = MutableLiveData<List<BlockState>>(emptyList())
    val dependencies = MutableLiveData<List<DependencyState>>(emptyList())
    
    override fun onResume() {
        disposable.add(eventBus.observe()
            .subscribe {
                if (it is GraphDragEvent) dragBlockHandler.handle(it)
                if (it is DependencyEvent) dependencyEventsHandler.handle(it)
            })
        
        disposable.add(observeBlocksUseCase.execute().subscribe { newBlocks ->
            val currentBlocks = blocks.value.orEmpty()
            
            blocks.value = newBlocks.map { newBlock ->
                currentBlocks.find { it.block == newBlock }
                    ?.copyWithInfo(block = newBlock)
                    ?: blockToNewGraphBlockMapper.map(newBlock)
            }
            dependencies.triggerRebuild()
        })
        
        disposable.add(observeDependenciesUseCase.execute().subscribe { dependencies ->
            this.dependencies.updateWith { current ->
                dependencies.map { dependency ->
                    current.orEmpty().find { it.dependency.id == dependency.id }
                        ?.copy(dependency = dependency)
                        ?: dependencyToDependencyStateMapper.map(dependency)
                }
            }
        })
    }
    
    override fun onPause() {
        disposable.clear()
    }
    
    fun onDropped(event: GraphDragEvent, dropPosition: Position) {
        val droppedInfo = event.dragInfo.copy(
            status = DRAG_DROP,
            to = GRAPH,
            position = dropPosition - event.dragInfo.dragTouch
        )
    
        eventBus.addEvent(event.copyWithDragInfo(droppedInfo))
    }
    
    fun onBlockMoved(blockId: BlockId, newPosition: Position) {
        moveBlockUseCase.execute(blockId, newPosition)
    }
    
    fun onCanceled(event: GraphDragEvent) {
        val cancelledInfo = event.dragInfo.copy(
            status = DRAG_CANCEL,
            to = GRAPH
        )
    
        eventBus.addEvent(event.copyWithDragInfo(cancelledInfo))
    }
    
    fun dependencyTipOnBlock(from: BlockId, to: BlockId) {
        blocks.updateWith {
            blocksWithHiddenBorders().findAndModify(
                { it.block.id == to },
                {
                    it.copyWithInfo(
                        border = BorderStatus(
                            isVisible = true,
                            isFailure = !checkIfDependencyPossible.execute(from, to)
                        )
                    )
                }
            )
        }
    }
    
    fun dependencyTipNotOnAnyBlock() {
        blocks.updateWith { blocksWithHiddenBorders() }
    }
    
    private fun blocksWithHiddenBorders(): List<BlockState> {
        return blocks.value.orEmpty().map {
            it.copyWithInfo(border = BorderStatus(isVisible = false))
        }
    }
    
    fun cancelCreatingDependency() {
        setMovingDependencyToIdle()
        dependencyTipNotOnAnyBlock()
    }
    
    fun addDependency(id: DependencyId, from: BlockId, to: BlockId) {
        setMovingDependencyToIdle()
        hideBorderOnBlock(to)
    
        addDependencyUseCase.execute(Dependency(id, from, to))
        eventBus.addEvent(OpenSetupDependency(id))
    }
    
    private fun hideBorderOnBlock(to: BlockId) {
        blocks.updateWith { current ->
            current.orEmpty().findAndModify({ it.block.id == to }) {
                it.copyWithInfo(border = it.border.copy(isVisible = false))
            }
        }
    }
    
    private fun setMovingDependencyToIdle() {
        movingDependency.updateWith { it?.copy(status = IDLE) }
    }
    
    fun getBlockState(blockId: BlockId): BlockState? {
        return blocks.value?.find { it.block.id == blockId }
    }
}
