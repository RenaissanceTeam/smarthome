package smarthome.client.presentation.scripts.addition.graph

import androidx.lifecycle.MutableLiveData
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
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.*
import smarthome.client.presentation.scripts.addition.graph.eventhandler.DependencyEventsHandler
import smarthome.client.presentation.scripts.addition.graph.eventhandler.DependencyEventsHandlerImpl
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_MOVE
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_START
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent

class DependencyEventsHandlerTest {
    
    private lateinit var handler: DependencyEventsHandler
    private val position1_1 = Position(1, 1)
    private val blockId = MockBlockId()
    private val otherBlockId = MockBlockId()
    
    private val dependencyId = MockDependencyId()
    private val dependency = MockDependency(dependencyId, blockId, otherBlockId)
    private val dependencyState = DependencyState(dependency)
    private lateinit var dependencyLiveData: MutableLiveData<MovingDependency>
    
    @Before
    fun setUp() {
        
        dependencyLiveData = mock {}
        handler = DependencyEventsHandlerImpl(
            dependencyLiveData
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
    fun `when create dependency should emit new moving dependency with status STARTED`() {
        val event = createDependencyEvent(startId = blockId, status = DEPENDENCY_START)
        
        handler.handle(event)
    
        verify(dependencyLiveData).value = argThat {
            this.status == STARTED
                && this.startBlock == blockId
                && this.rawEndPosition == position1_1
        }
    }
    
    @Test
    fun `when dependency move should update moving dependency with new position`() {
        val rawEndPosition = Position(22, 22)
    
        whenever(dependencyLiveData.value).then {
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
    
        verify(dependencyLiveData).value = argThat {
            this.status == MOVING
                && this.startBlock == blockId
                && this.rawEndPosition == rawEndPosition
        }
    }
    
    
}