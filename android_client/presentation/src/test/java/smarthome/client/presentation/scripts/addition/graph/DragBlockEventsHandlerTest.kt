package smarthome.client.presentation.scripts.addition.graph

import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import smarthome.client.domain.api.scripts.usecases.AddBlockToScriptGraphUseCase
import smarthome.client.domain.api.scripts.usecases.MoveBlockUseCase
import smarthome.client.domain.api.scripts.usecases.RemoveBlockUseCase
import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Position
import smarthome.client.presentation.containsThat
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.addition.graph.events.drag.*

class DragBlockEventsHandlerTest {
    private lateinit var handler: DragBlockEventsHandler
    private lateinit var getCurrentBlocks: () -> List<BlockState>
    private lateinit var getBlockStateForEvent: (GraphDragEvent) -> BlockState?
    private lateinit var emitBlocks: (List<BlockState>) -> Unit
    private lateinit var moveBlockUseCase: MoveBlockUseCase
    private lateinit var removeBlockUseCase: RemoveBlockUseCase
    private lateinit var addBlockToScriptGraphUseCase: AddBlockToScriptGraphUseCase
    private lateinit var addBlockHelper: AddBlockHelper
    
    
    private val position1_1 = Position(1, 1)
    private lateinit var block: MockBlockState
    private val blockId = MockBlockId()
    private lateinit var currentBlocks: List<BlockState>
    
    @Before
    fun setUp() {
        emitBlocks = mock {}
        moveBlockUseCase = mock {}
        removeBlockUseCase = mock {}
        addBlockToScriptGraphUseCase = mock {}
        addBlockHelper = mock {}
        block = setupMockingBlock()
        currentBlocks = listOf(block)
        getCurrentBlocks = mock {
            on { invoke() }.then { currentBlocks }
        }
        handler = DragBlockEventsHandlerImpl(
            getCurrentBlocks,
            emitBlocks,
            moveBlockUseCase,
            removeBlockUseCase,
            addBlockToScriptGraphUseCase,
            addBlockHelper
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
    
    private fun setupMockingBlock(id: MockBlockId = blockId,
                                  position: Position = position1_1): MockBlockState {
        return MockBlockState(id, position)
    }
    
    private fun verifyEmitBlock(id: BlockId = blockId,
                                predicate: (BlockState) -> Boolean) {
        verify(emitBlocks).invoke(argThat {
            val block = find { it.block.id == id } ?: return@argThat false
            predicate(block)
        })
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
    fun `when drop controller from hub should add block and emit`() {
        val dropEvent =
            createDragEvent(status = DRAG_DROP, to = GRAPH, from = CONTROLLERS_HUB)
        
        
        handler.handle(dropEvent)
        
        verifyEmitBlock { draggedBlock ->
            draggedBlock is MockBlockState
                && draggedBlock.id == blockId
                && draggedBlock.position == position1_1
        }
    }
    
    @Test
    fun `when drag from graph is cancelled should make block visible`() {
        val dragEvent = createDragEvent(status = DRAG_CANCEL, from = GRAPH)
        
        handler.handle(dragEvent)
    
        verifyEmitBlock { it is MockBlockState && it.visible }
    }
    
    @Test
    fun `when drag from graph and drop to graph should emit block with new position`() {
        val droppedAt = Position(12, 22)
        val dropEvent =
            createDragEvent(status = DRAG_DROP, to = GRAPH, from = GRAPH, position = droppedAt)
        
        
        handler.handle(dropEvent)
        
        verifyEmitBlock {
            it.visible && it.block.position == droppedAt
        }
    }
    
    @Test
    fun `when drag drop from graph should remove block`() {
        val dropEvent = createDragEvent(status = DRAG_DROP, to = CONTROLLERS_HUB, from = GRAPH)
        
        handler.handle(dropEvent)
    
        verify(emitBlocks).invoke(argThat { !containsThat { it.block.id == blockId } })
    }
    
}