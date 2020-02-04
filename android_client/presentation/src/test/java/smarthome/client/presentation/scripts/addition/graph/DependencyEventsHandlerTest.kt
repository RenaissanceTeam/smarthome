package smarthome.client.presentation.scripts.addition.graph

import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test
import smarthome.client.presentation.scripts.addition.graph.blockviews.controller.ControllerBlock
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyState
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MovingDependencyTipStatus
import smarthome.client.presentation.scripts.addition.graph.events.drag.CommonDragInfo
import smarthome.client.presentation.scripts.addition.graph.events.drag.DRAG_START
import smarthome.client.presentation.scripts.addition.graph.events.drag.GRAPH
import smarthome.client.presentation.scripts.addition.graph.events.drag.UNKNOWN
import smarthome.client.presentation.util.Position
import kotlin.test.assertTrue

class DependencyEventsHandlerTest {
    
    private lateinit var handler: DependencyEventsHandler
    private lateinit var emitDependencies: (MutableMap<String, DependencyState>) -> Unit
    private lateinit var getCurrentDependencies: () -> MutableMap<String, DependencyState>
    private lateinit var emitTipStatus: (MovingDependencyTipStatus) -> Unit
    private lateinit var getCurrentTipStatus: () -> MovingDependencyTipStatus
    private val position1_1 = Position(1f, 1f)
    private val blockId = MockBlockId()
    
    @Before
    fun setUp() {
        emitDependencies = mock {}
        getCurrentDependencies = mock {}
        emitTipStatus = mock {}
        getCurrentTipStatus = mock {}
        handler = DependencyEventsHandler(
            emitDependencies = emitDependencies,
            getCurrentTipStatus = getCurrentTipStatus,
            emitTipStatus = emitTipStatus,
            getCurrentDependencies = getCurrentDependencies
        )
    }
}