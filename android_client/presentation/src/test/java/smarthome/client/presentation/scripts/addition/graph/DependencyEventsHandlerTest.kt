package smarthome.client.presentation.scripts.addition.graph

import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.DependencyId
import smarthome.client.entity.script.Position
import smarthome.client.entity.script.emptyPosition
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyState
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.IDLE
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MOVING
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MovingDependency
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_MOVE
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_START
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent

class DependencyEventsHandlerTest {
    
    private lateinit var handler: DependencyEventsHandler
    private lateinit var emitMovingDependency: (MovingDependency) -> Unit
    private lateinit var getCurrentMovingDependency: () -> MovingDependency
    private val position1_1 = Position(1, 1)
    private val blockId = MockBlockId()
    private val otherBlockId = MockBlockId()
    
    private val dependencyId = MockDependencyId()
    private val dependency = MockDependency(dependencyId, blockId, otherBlockId)
    private val dependencyState = DependencyState(dependency)
    
    @Before
    fun setUp() {
        emitMovingDependency = mock {}
        getCurrentMovingDependency = mock {}
        handler = DependencyEventsHandlerImpl(
            getCurrentMovingDependency = getCurrentMovingDependency,
            emitMovingDependency = emitMovingDependency
        )
    }
    
    private fun createMovingDependency(id: DependencyId = dependencyId,
                                       startBlock: BlockId? = blockId,
                                       status: String = IDLE,
                                       rawEndPosition: Position? = null): MovingDependency {
        return MovingDependency(id, startBlock, status, rawEndPosition)
    }
    
    private fun createDependencyEvent(
        id: DependencyId = dependencyId,
        status: String = DEPENDENCY_START,
        startId: BlockId,
        endId: BlockId? = null,
        rawEndPosition: Position = position1_1
    ): DependencyEvent {
        return DependencyEvent(id, status, startId, endId, rawEndPosition)
    }
    
    @Test
    fun `when create dependency should emit new moving dependency with status MOVING`() {
        val event = createDependencyEvent(startId = blockId, status = DEPENDENCY_START)
        
        handler.handle(event)
    
        verify(emitMovingDependency).invoke(argThat {
            this.status == MOVING
                && this.startBlock == blockId
                && this.rawEndPosition == position1_1
        })
    }
    
    @Test
    fun `when dependency move should update moving dependency with new position`() {
        val rawEndPosition = Position(22, 22)
    
        whenever(getCurrentMovingDependency.invoke()).then {
            createMovingDependency(
                startBlock = blockId,
                status = IDLE,
                rawEndPosition = emptyPosition
            )
        }
        
        handler.handle(createDependencyEvent(
            startId = blockId,
            status = DEPENDENCY_MOVE,
            rawEndPosition = rawEndPosition
        ))
    
        verify(emitMovingDependency).invoke(argThat {
            this.status == MOVING
                && this.startBlock == blockId
                && this.rawEndPosition == rawEndPosition
        })
    }
    
    
}