package smarthome.client.presentation.scripts.addition.graph

import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyState
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MovingDependencyTipStatus
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_MOVE
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_START
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent
import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.util.Position
import kotlin.test.assertEquals

class DependencyEventsHandlerTest {
    
    private lateinit var handler: DependencyEventsHandler
    private lateinit var emitDependencies: (MutableMap<String, DependencyState>) -> Unit
    private lateinit var getCurrentDependencies: () -> MutableMap<String, DependencyState>
    private lateinit var emitTipStatus: (MovingDependencyTipStatus) -> Unit
    private lateinit var getCurrentTipStatus: () -> MovingDependencyTipStatus
    private val position1_1 = Position(1f, 1f)
    private val blockId = MockBlockId()
    private val dependencyId = "deip"
    private lateinit var mockDependency: DependencyState
    private lateinit var currentDeps: MutableMap<String, DependencyState>
    
    
    @Before
    fun setUp() {
        emitDependencies = mock {}
        mockDependency = createDependencyState()
        currentDeps = mutableMapOf(dependencyId to mockDependency)
        getCurrentDependencies = mock {
            on { invoke() }.then { currentDeps }
        }
        emitTipStatus = mock {}
        getCurrentTipStatus = mock {}
        handler = DependencyEventsHandler(
            emitDependencies = emitDependencies,
            getCurrentTipStatus = getCurrentTipStatus,
            emitTipStatus = emitTipStatus,
            getCurrentDependencies = getCurrentDependencies
        )
    }
    
    private fun createDependencyState(id: String = dependencyId,
                                      startBlock: GraphBlockIdentifier? = null,
                                      endBlock: GraphBlockIdentifier? = null,
                                      rawEndPosition: Position? = null): DependencyState {
        return DependencyState(id, startBlock, endBlock, rawEndPosition)
    }
    
    private fun createDependencyEvent(
        id: String = dependencyId,
        status: String = DEPENDENCY_START,
        startId: GraphBlockIdentifier,
        endId: GraphBlockIdentifier? = null,
        rawEndPosition: Position = position1_1
    ): DependencyEvent {
        return DependencyEvent(id, status, startId, endId, rawEndPosition)
    }
    
    private fun verifyEmitDependency(id: String = dependencyId,
                                     predicate: (DependencyState) -> Boolean) {
        verify(emitDependencies).invoke(argThat {
            val state = getCurrentDependencies()[id] ?: return@argThat false
            
            predicate(state)
        })
    }
    
    @Test
    fun `when create dependency should update dependencies with added item`() {
        val event = createDependencyEvent(startId = blockId, status = DEPENDENCY_START)
        
        handler.handle(event)
        
        verifyEmitDependency {
            it.startBlock == blockId && it.rawEndPosition == position1_1
        }
    }
    
    @Test
    fun `when dependency move should update dependencies with new position`() {
        val rawEndPosition = Position(22f, 22f)
        
        handler.handle(createDependencyEvent(
            startId = blockId,
            status = DEPENDENCY_MOVE,
            rawEndPosition = rawEndPosition
        ))
        
        verifyEmitDependency {
            it.rawEndPosition == rawEndPosition
        }
    }
    
    
}