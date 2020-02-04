package smarthome.client.presentation.scripts.addition.graph

import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlock
import smarthome.client.presentation.scripts.addition.graph.events.drag.*
import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.util.Position

class DragBlockHandlerTest {
    private lateinit var handler: DragBlockHandler
    private lateinit var getCurrentBlocks: () -> MutableMap<GraphBlockIdentifier, GraphBlock>
    private lateinit var getBlockForEvent: (GraphDragEvent) -> GraphBlock?
    private lateinit var emitBlocks: (MutableMap<GraphBlockIdentifier, GraphBlock>) -> Unit
    private val position1_1 = Position(1f, 1f)
    private lateinit var block: MockGraphBlock
    private val blockId = MockBlockId()
    private lateinit var currentBlocks: MutableMap<GraphBlockIdentifier, GraphBlock>
    
    @Before
    fun setUp() {
        emitBlocks = mock {}
        block = setupMockingBlock()
        currentBlocks = mutableMapOf(blockId to block)
        getCurrentBlocks = mock {
            on { invoke() }.then { currentBlocks }
        }
        handler = DragBlockHandler(getCurrentBlocks, emitBlocks)
    }
    
    private fun createDragEvent(id: GraphBlockIdentifier = blockId,
                                status: String,
                                from: String = UNKNOWN,
                                to: String = UNKNOWN,
                                touchPosition: Position = position1_1,
                                position: Position = position1_1): MockDragEvent {
        return MockDragEvent(CommonDragInfo(id, status, from, to, touchPosition, position))
    }
    
    private fun setupMockingBlock(id: MockBlockId = blockId,
                                  position: Position = position1_1): MockGraphBlock {
        return MockGraphBlock(id, position)
    }
    
    private fun verifyEmitBlock(id: GraphBlockIdentifier = blockId,
                                predicate: (GraphBlock) -> Boolean) {
        verify(emitBlocks).invoke(argThat {
            val block = this[id] ?: return@argThat false
            predicate(block)
        })
    }
    
    
    @Test
    fun `when start drag on graph should hide dragged block`() {
        val dragEvent = createDragEvent(status = DRAG_START, from = GRAPH)
        
        handler.handle(dragEvent)
        verifyEmitBlock {
            it is MockGraphBlock && !it.visible
        }
    }
    
    
    @Test
    fun `when drop controller from hub should add block and emit`() {
        val dropEvent =
            createDragEvent(status = DRAG_DROP, to = GRAPH, from = CONTROLLERS_HUB)
        
        
        handler.handle(dropEvent)
        
        verifyEmitBlock { draggedBlock ->
            draggedBlock is MockGraphBlock
                && draggedBlock.id == blockId
                && draggedBlock.position == position1_1
        }
    }
    
    @Test
    fun `when drag from graph is cancelled should make block visible`() {
        val dragEvent = createDragEvent(status = DRAG_CANCEL, from = GRAPH)
        
        handler.handle(dragEvent)
        
        verifyEmitBlock { it is MockGraphBlock && it.visible }
    }
    
    @Test
    fun `when drag from graph and drop to graph should emit block with new position`() {
        val droppedAt = Position(12f, 22f)
        val dropEvent =
            createDragEvent(status = DRAG_DROP, to = GRAPH, from = GRAPH, position = droppedAt)
        
        
        handler.handle(dropEvent)
        
        verifyEmitBlock {
            it.visible && it.position == droppedAt
        }
    }
    
    @Test
    fun `when drag drop from graph should remove block`() {
        val dropEvent = createDragEvent(status = DRAG_DROP, to = CONTROLLERS_HUB, from = GRAPH)
        
        handler.handle(dropEvent)
        
        verify(emitBlocks).invoke(argThat { blockId !in this })
    }
    
}