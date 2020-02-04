package smarthome.client.presentation.scripts.addition.graph

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import smarthome.client.domain.api.scripts.usecases.CheckIfDependencyPossibleUseCase
import smarthome.client.domain.api.scripts.usecases.ObserveBlocksUseCase
import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Position
import smarthome.client.presentation.findAndModify
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyState
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MovingDependencyTipStatus
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BorderStatus
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlockResolver
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.DRAG_CANCEL
import smarthome.client.presentation.scripts.addition.graph.events.drag.DRAG_DROP
import smarthome.client.presentation.scripts.addition.graph.events.drag.GRAPH
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationParamLiveData

class ScriptGraphViewModel : KoinViewModel() {
    
    private val eventBus: GraphEventBus by inject()
    private val blockResolver: GraphBlockResolver by inject()
    private val observeBlocksUseCase: ObserveBlocksUseCase by inject()
    private val checkIfDependencyPossible: CheckIfDependencyPossibleUseCase by inject()
    private val blockToNewGraphBlockMapper: BlockToNewGraphBlockStateMapper by inject()
    private val dragBlockHandler: DragBlockEventsHandler by inject()
    private val dependencyEventsHandler: DependencyEventsHandler by inject()
    
    
    val blocks = MutableLiveData<List<BlockState>>()
    val dependencies = MutableLiveData<MutableMap<String, DependencyState>>()
    val movingDependencyTip = MutableLiveData<MovingDependencyTipStatus>()
    val setupDependency = NavigationParamLiveData<DependencyState>()
    
    init {
        
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
        })
    }
    
    private fun getCurrentDependencies(): MutableMap<String, DependencyState> {
        return dependencies.value ?: mutableMapOf()
    }
    
    private fun getCurrentTipStatus(): MovingDependencyTipStatus {
        return movingDependencyTip.value ?: MovingDependencyTipStatus()
    }
    
    fun onDropped(event: GraphDragEvent, dropPosition: Position) {
        val droppedInfo = event.dragInfo.copy(
            status = DRAG_DROP,
            to = GRAPH,
            position = dropPosition - event.dragInfo.dragTouch
        )
        val dropEvent = event.copyWithDragInfo(droppedInfo)
        
        eventBus.addEvent(dropEvent)
    }
    
    fun onCanceled(event: GraphDragEvent) {
        val cancelledInfo = event.dragInfo.copy(
            status = DRAG_CANCEL,
            to = GRAPH
        )
        val cancelEvent = event.copyWithDragInfo(cancelledInfo)
    
        eventBus.addEvent(cancelEvent)
    }
    
    fun dependencyTipOnBlock(from: BlockId, to: BlockId) {
        val blocks = blocksWithHiddenBorders()
        val block = blocks.find { it.block.id == to } ?: return
        
        blocks.findAndModify({ it.block.id == to }) {
            block.copyWithInfo(
                border = BorderStatus(
                    isVisible = true,
                    isFailure = checkIfDependencyPossible.execute(from, to)
                )
            )
        }
        
        this.blocks.value = blocks
    }
    
    fun dependencyTipNotOnAnyBlock() {
        this.blocks.value = blocksWithHiddenBorders()
    }
    
    private fun blocksWithHiddenBorders(): List<BlockState> {
        val blocks = this.blocks.value.orEmpty()
        return blocks.map {
            it.copyWithInfo(border = BorderStatus(isVisible = false))
        }
    }
    
    fun cancelCreatingDependency(id: String) {
        setDependencyCreationToDefault()
        
        val dependencies = getCurrentDependencies()
        dependencies.remove(id)
        
        this.dependencies.value = dependencies
    }
    
    fun addDependency(id: String, from: BlockId, to: BlockId) {
        setDependencyCreationToDefault()
        emitMovedDependencyWithSetEndBlock(id, to)
        hideBorderOnBlock(to)
    }
    
    private fun hideBorderOnBlock(to: BlockId) {
        val blocks = this.blocks.value.orEmpty()
        blocks.findAndModify({ it.block.id == to }) {
            val border = it.border
            it.copyWithInfo(border = border.copy(isVisible = false))
        }
        this.blocks.value = blocks
    }
    
    private fun emitMovedDependencyWithSetEndBlock(id: String, to: BlockId) {
        val dependencies = getCurrentDependencies()
        val movingDependency = dependencies[id] ?: return
        
        val finishedDependency = movingDependency.copy(endBlock = to, rawEndPosition = null)
        dependencies[id] = finishedDependency
        this.dependencies.value = dependencies
    }
    
    private fun setDependencyCreationToDefault() {
        movingDependencyTip.value = MovingDependencyTipStatus()
    }
}

