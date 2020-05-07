package smarthome.client.presentation.scripts.setup.graph

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.scripts.usecases.setup.AddDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.setup.CheckIfDependencyPossibleUseCase
import smarthome.client.domain.api.scripts.usecases.setup.ObserveBlocksUseCase
import smarthome.client.domain.api.scripts.usecases.setup.ObserveDependenciesUseCase
import smarthome.client.entity.Controller
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.presentation.scripts.setup.graph.blockviews.dependency.IDLE
import smarthome.client.presentation.scripts.setup.graph.blockviews.dependency.MOVING
import smarthome.client.presentation.scripts.setup.graph.blockviews.dependency.MovingDependency
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.setup.graph.eventhandler.DependencyEventsHandler
import smarthome.client.presentation.scripts.setup.graph.eventhandler.DragBlockEventsHandler
import smarthome.client.presentation.scripts.setup.graph.events.GraphEvent
import smarthome.client.presentation.scripts.setup.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.setup.graph.events.dependency.DEPENDENCY_MOVE
import smarthome.client.presentation.scripts.setup.graph.events.dependency.DEPENDENCY_START
import smarthome.client.presentation.scripts.setup.graph.events.dependency.DependencyEvent
import smarthome.client.presentation.scripts.setup.graph.events.drag.*
import smarthome.client.presentation.scripts.setup.graph.events.navigation.OpenSetupDependency
import smarthome.client.presentation.scripts.setup.graph.mapper.BlockToNewGraphBlockStateMapper
import smarthome.client.presentation.scripts.setup.graph.view.GraphViewModel
import smarthome.client.util.DataStatus
import smarthome.client.util.Position
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GraphViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var observeControllerUseCase: ObserveControllerUseCase
    private lateinit var eventBus: GraphEventBus
    private lateinit var events: PublishSubject<GraphEvent>
    private val position1_1 = Position(1, 1)
    private lateinit var viewModel: GraphViewModel
    private lateinit var observeBlocksUseCase: ObserveBlocksUseCase
    private lateinit var observeDependenciesUseCase: ObserveDependenciesUseCase
    private lateinit var dragBlockEventsHandler: DragBlockEventsHandler
    private lateinit var dependencyEventsHandler: DependencyEventsHandler
    private lateinit var checkIfDependencyPossibleUseCase: CheckIfDependencyPossibleUseCase
    private lateinit var addDependencyUseCase: AddDependencyUseCase
    private lateinit var blockToNewGraphBlockStateMapper: BlockToNewGraphBlockStateMapper
    private lateinit var blocksObservable: PublishSubject<List<Block>>
    private lateinit var dependenciesObservable: PublishSubject<List<Dependency>>
    private val blockId = "blockId"
    private val otherBlockId = "otherBlockId"
    private val block = MockBlock(blockId, position1_1)
    private val otherBlock = MockBlock(otherBlockId, position1_1)
    private val blockState = MockBlockState(block)
    private val otherBlockState = MockBlockState(otherBlock)
    private val dependencyId = "depId"

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        blocksObservable = PublishSubject.create()
        dependenciesObservable = PublishSubject.create()
        observeControllerUseCase =
                mock { on { execute(any()) }.then { Observable.empty<DataStatus<Controller>>() } }
        observeBlocksUseCase = mock {
            on { execute() }.then { blocksObservable }
        }
        observeDependenciesUseCase = mock {
            on { execute() }.then { dependenciesObservable }
        }
        events = PublishSubject.create()
        eventBus = mock { on { observe() }.then { events } }
        dragBlockEventsHandler = mock {}
        addDependencyUseCase = mock {}
        dependencyEventsHandler = mock {}
        checkIfDependencyPossibleUseCase = mock {
            on { execute(any(), any()) }.then { true }
        }
        blockToNewGraphBlockStateMapper = mock {
            on { map(any()) }.then { blockState }
        }
        startKoin {
            modules(module {
                single { observeControllerUseCase }
                single { eventBus }
                single { observeBlocksUseCase }
                single { dragBlockEventsHandler }
                single { dependencyEventsHandler }
                single { blockToNewGraphBlockStateMapper }
                single { checkIfDependencyPossibleUseCase }
                single { observeDependenciesUseCase }
                single { addDependencyUseCase }
            })
        }

        viewModel = GraphViewModel()
    }


    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `when call to drop should emit drop event for event bus`() {
        val event = createDragEvent(status = DRAG_START, from = CONTROLLERS_HUB)
        viewModel.onDropped(event, position1_1)

        verify(eventBus).addEvent(argThat {
            this is BlockDragEvent
                    && this.from == blockId
                    && this.to == GRAPH
                    && this.status == DRAG_DROP
        })
    }

    @Test
    fun `when drop block should calculate position as drop position minus touch position`() {
        val event = createDragEvent(status = DRAG_START, from = CONTROLLERS_HUB)

        viewModel.onDropped(event, position1_1)
        verify(eventBus).addEvent(argThat {
            this is BlockDragEvent
                    && this.position == Position(0, 0)
        })
    }

    private fun createDragEvent(
            block: Block = this.block,
            status: String,
            from: String = UNKNOWN,
            to: String = UNKNOWN,
            touchPosition: Position = position1_1,
            position: Position = position1_1): BlockDragEvent {
        return BlockDragEvent(
                block = block,
                status = status,
                from = from,
                to = to,
                dragTouch = touchPosition,
                position = position
        )
    }

    private fun assertHasBlockValue(id: String = blockId): BlockState {
        val blocks = viewModel.blocks.value
        assertNotNull(blocks)

        val addedBlock = blocks.find { it.block.id == id }
        assertNotNull(addedBlock)

        return addedBlock
    }

    @Test
    fun `when dependency tip on some block should emit with visible border`() {
        addBlock()
        viewModel.dependencyTipOnBlock(from = otherBlockId, to = blockId)

        val block = assertHasBlockValue(blockId)
        assertTrue(block.border.isVisible)
    }

    private fun addBlock() {
        blocksObservable.onNext(listOf(block))
    }

    private fun addTwoBlocks() {
        blocksObservable.onNext(listOf(block, otherBlock))
    }

    @Test
    fun `when end creating dependency on graph then should emit idle moving dependency`() {
        viewModel.movingDependency.value = MovingDependency(
                "movingId",
                otherBlockId,
                MOVING,
                position1_1
        )

        viewModel.cancelCreatingDependency()

        val dependency = viewModel.movingDependency.value
        assertNotNull(dependency)
        assertTrue { dependency.status == IDLE }
    }

    @Test
    fun `when end creating dependency on other block should emit on eventbus`() {
        viewModel.tryAddDependency(dependencyId, otherBlockId, blockId)

        verify(eventBus).addEvent(argThat {
            this is OpenSetupDependency && this.id == dependencyId
        })
    }

    @Test
    fun `when add dependency should call add dependency use case`() {
        viewModel.tryAddDependency(dependencyId, otherBlockId, blockId)


        verify(addDependencyUseCase).execute(
                argThat {
                    this.id == dependencyId
                            && this.startBlock == otherBlockId
                            && this.endBlock == blockId
                }
        )
    }

    private fun emitMoveDependencyFromOther() {
        events.onNext(DependencyEvent(
                id = dependencyId,
                startId = otherBlockId,
                status = DEPENDENCY_MOVE,
                rawEndPosition = Position(22, 22)
        ))
    }

    private fun emitStartDependencyFromOther() {
        events.onNext(DependencyEvent(
                id = dependencyId,
                startId = otherBlockId,
                status = DEPENDENCY_START,
                rawEndPosition = position1_1
        ))
    }

    @Test
    fun `when add dependency should hide border on the added block`() {
        addBlock()
        emitStartDependencyFromOther()
        emitMoveDependencyFromOther()

        viewModel.dependencyTipOnBlock(otherBlockId, blockId)
        assertTrue(assertHasBlockValue(blockId).border.isVisible)

        viewModel.tryAddDependency(dependencyId, otherBlockId, blockId)
        assertFalse(assertHasBlockValue(blockId).border.isVisible)
    }

    @Test
    fun `when remove block with dependency should remove associated dependencies`() {

    }
}