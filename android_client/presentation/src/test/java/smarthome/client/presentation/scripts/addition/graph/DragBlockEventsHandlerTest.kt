package smarthome.client.presentation.scripts.addition.graph

import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import smarthome.client.domain.api.scripts.usecases.AddBlockToScriptGraphUseCase
import smarthome.client.domain.api.scripts.usecases.MoveBlockUseCase
import smarthome.client.domain.api.scripts.usecases.RemoveBlockUseCase
import smarthome.client.entity.script.Block
import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Position
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.addition.graph.events.drag.*

class DragBlockEventsHandlerTest {
    private lateinit var handler: DragBlockEventsHandler
    private lateinit var getBlockStateForEvent: (GraphDragEvent) -> BlockState?
    private lateinit var moveBlockUseCase: MoveBlockUseCase
    private lateinit var removeBlockUseCase: RemoveBlockUseCase
    private lateinit var addBlockToScriptGraphUseCase: AddBlockToScriptGraphUseCase
    private lateinit var addBlockHelper: AddBlockHelper
    private lateinit var addGraphBlockStateHelper: AddGraphBlockStateHelper
    private lateinit var block: Block
    private lateinit var blocksLiveData: MutableLiveData<List<BlockState>>
    
    private val position1_1 = Position(1, 1)
    private lateinit var blockState: MockBlockState
    private val blockId = MockBlockId()
    private lateinit var currentBlocks: List<BlockState>
    
    @Before
    fun setUp() {
        block = MockBlock(blockId, position1_1)
        blockState = MockBlockState(block)
        moveBlockUseCase = mock {
            on { execute(any(), any(), any()) }.then { block }
        }
        removeBlockUseCase = mock {}
        addGraphBlockStateHelper = mock {}
        addBlockToScriptGraphUseCase = mock {}
        addBlockHelper = mock {
            on { resolveAddingFromEvent(any(), any()) }.then { block }
        }
    
    
        currentBlocks = listOf(blockState)
        blocksLiveData = mock {
            on { value }.then { currentBlocks }
        }
    
        handler = DragBlockEventsHandlerImpl(
            blocksLiveData,
            moveBlockUseCase,
            removeBlockUseCase,
            addBlockToScriptGraphUseCase,
            addBlockHelper,
            addGraphBlockStateHelper
        )
    }
    
    private fun createDragEvent(id: BlockId = blockId,
                                status: String,
                                from: String = UNKNOWN,
                                to: String = UNKNOWN,
                                touchPosition: Position = position1_1,
                                position: Position = position1_1): MockDragEvent {
        return MockDragEvent(CommonDragInfo(id, status, from, to, touchPosition, position))
    }
    
    private fun verifyEmitBlock(id: BlockId = blockId,
                                predicate: (BlockState) -> Boolean) {
        verify(blocksLiveData).value = argThat {
            val block = find { it.block.id == id } ?: return@argThat false
            predicate(block)
        }
    }
    
    
    @Test
    fun `when start drag on graph should hide dragged block`() {
        val dragEvent = createDragEvent(status = DRAG_START, from = GRAPH)
        
        handler.handle(dragEvent)
        verifyEmitBlock {
            it is MockBlockState && !it.visible
        }
    }
    
    @Test
    fun `when drop controller from hub should call add helper`() {
        val dropEvent =
            createDragEvent(status = DRAG_DROP, to = GRAPH, from = CONTROLLERS_HUB)
    
    
        handler.handle(dropEvent)
    
        verify(addBlockHelper).resolveAddingFromEvent(any(), eq(dropEvent))
    }
    
    @Test
    fun `when drop controller from hub should add block and emit`() {
        val dropEvent =
            createDragEvent(status = DRAG_DROP, to = GRAPH, from = CONTROLLERS_HUB)
        
        whenever(addBlockHelper.resolveAddingFromEvent(any(), eq(dropEvent))).then {
            block.copyWithPosition(dropEvent.dragInfo.position)
        }
        
        handler.handle(dropEvent)
        
        verifyEmitBlock { draggedBlock ->
            draggedBlock is MockBlockState
                && draggedBlock.block.id == blockId
                && draggedBlock.block.position == position1_1
        }
    }
    
    @Test
    fun `when drag from graph is cancelled should make block visible`() {
        val dragEvent = createDragEvent(status = DRAG_CANCEL, from = GRAPH)
        
        handler.handle(dragEvent)
    
        verifyEmitBlock { it is MockBlockState && it.visible }
    }
    
    @Test
    fun `when drag n drop inside graph should call moveBlockUseCase`() {
        val droppedAt = Position(12, 22)
        val dropEvent =
            createDragEvent(status = DRAG_DROP, to = GRAPH, from = GRAPH, position = droppedAt)
        
        handler.handle(dropEvent)
        
        verify(moveBlockUseCase).execute(any(), eq(blockId), eq(droppedAt))
    }
    @Test
    fun `when drag from graph and drop to graph should emit block with new position`() {
        val droppedAt = Position(12, 22)
        val dropEvent =
            createDragEvent(status = DRAG_DROP, to = GRAPH, from = GRAPH, position = droppedAt)
    
        whenever(moveBlockUseCase.execute(any(), any(), eq(droppedAt))).then {
            block.copyWithPosition(droppedAt)
        }
        
        handler.handle(dropEvent)
        
        verifyEmitBlock {
            it.visible && it.block.position == droppedAt
        }
    }
    
    @Test
    fun `when drag drop from graph should call remove block usecase`() {
        val dropEvent = createDragEvent(status = DRAG_DROP, to = CONTROLLERS_HUB, from = GRAPH)
        
        handler.handle(dropEvent)
    
        verify(removeBlockUseCase).execute(any(), eq(blockId))
    }
    
}