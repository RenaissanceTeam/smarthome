package smarthome.client.presentation.scripts.addition.graph

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyState
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MovingDependencyTipStatus
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BorderStatus
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlock
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlockResolver
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.*
import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.triggerRebuild
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationParamLiveData
import smarthome.client.presentation.util.Position

class ScriptGraphViewModel : KoinViewModel() {
    
    private val eventBus: GraphEventBus by inject()
    private val blockResolver: GraphBlockResolver by inject()
    val blocks = MutableLiveData<MutableMap<GraphBlockIdentifier, GraphBlock>>(mutableMapOf())
    val dependencies = MutableLiveData<MutableMap<String, DependencyState>>(mutableMapOf())
    val movingDependencyTip = MutableLiveData<MovingDependencyTipStatus>()
    val setupDependency = NavigationParamLiveData<DependencyState>()
    
    init {
        val dragBlockHandler = DragBlockHandler(
            emitBlocks = {
                blocks.value = it
                dependencies.triggerRebuild()
            },
            getCurrentBlocks = ::getCurrentBlocks
        )
        
        val dependencyEventsHandler = DependencyEventsHandler(
            emitDependencies = { dependencies.value = it },
            getCurrentDependencies = ::getCurrentDependencies,
            emitTipStatus = { movingDependencyTip.value = it },
            getCurrentTipStatus = ::getCurrentTipStatus
        )
        
        disposable.add(eventBus.observe()
            .subscribe {
                if (it is GraphDragEvent) dragBlockHandler.handle(it)
                if (it is DependencyEvent) dependencyEventsHandler.handle(it)
        })
    }
    
    private fun getCurrentBlocks(): MutableMap<GraphBlockIdentifier, GraphBlock> {
        return blocks.value ?: mutableMapOf()
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
    
    fun dependencyTipOnBlock(from: GraphBlockIdentifier, to: GraphBlockIdentifier) {
        val blocks = blocksWithHiddenBorders()
        val block = blocks[to] ?: return
        
        blocks[to] = block.copyWithInfo(
            border = BorderStatus(
                isVisible = true,
                isFailure = resolveIsImpossibleDependency(from, to)
            )
        )
        this.blocks.value = blocks
    }
    
    private fun resolveIsImpossibleDependency(from: GraphBlockIdentifier,
                                              to: GraphBlockIdentifier): Boolean {
        return false
    }
    
    fun dependencyTipNotOnAnyBlock() {
        this.blocks.value = blocksWithHiddenBorders()
    }
    
    private fun blocksWithHiddenBorders(): MutableMap<GraphBlockIdentifier, GraphBlock> {
        val blocks = getCurrentBlocks()
        blocks.keys.forEach { id ->
            blocks[id] = blocks[id]!!.copyWithInfo(border = BorderStatus(isVisible = false))
        }
        return blocks
    }
    
    fun cancelCreatingDependency(id: String) {
        setDependencyCreationToDefault()
        
        val dependencies = getCurrentDependencies()
        dependencies.remove(id)
        
        this.dependencies.value = dependencies
    }
    
    fun addDependency(id: String, from: GraphBlockIdentifier, to: GraphBlockIdentifier) {
        setDependencyCreationToDefault()
        emitMovedDependencyWithSetEndBlock(id, to)
        hideBorderOnToBlock(to)
    }
    
    private fun hideBorderOnToBlock(to: GraphBlockIdentifier) {
        val blocks = getCurrentBlocks()
        val toBlock = blocks[to] ?: return
        blocks[to] = toBlock.copyWithInfo(border = toBlock.border.copy(isVisible = false))
        this.blocks.value = blocks
    }
    
    private fun emitMovedDependencyWithSetEndBlock(id: String,
                                                   to: GraphBlockIdentifier) {
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

