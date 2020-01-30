package smarthome.client.presentation.scripts.addition.graph.blockviews

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.entity.Controller
import smarthome.client.presentation.util.Position
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.drag.CommonDragInfo
import smarthome.client.presentation.scripts.addition.graph.events.drag.ControllerDragEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.DRAG_START
import smarthome.client.presentation.scripts.addition.graph.events.drag.GRAPH
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.ControllerBlock
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.util.LoadingStatus
import smarthome.client.util.data
import kotlin.properties.Delegates

class GraphControllerViewModel: KoinViewModel() {
    
    val visible = MutableLiveData<Boolean>()
    val dragVisible = MutableLiveData<Boolean>(true)
    val position =
        MutableLiveData<Position>()
    val loading = MutableLiveData<Boolean>()
    val data =
        MutableLiveData<Controller>()
    
    private val eventBus: GraphEventBus by inject()
    private val observeController: ObserveControllerUseCase by inject()
    private var id by Delegates.observable<Long?>(
        null) { _, oldId, newId ->
        if (oldId != newId && newId != null) observeController(newId)
    }
    
    
    fun onNewBlockData(block: ControllerBlock) {
        // todo on first init observe controller
    
        id = block.id.id
        visible.value = block.visible
        position.value = block.position
        
        // add live data for state
    }
    
    private fun observeController(id: Long) {
        disposable.clear()
        disposable.add(observeController.execute(id).subscribe { datastatus ->
            loading.value = datastatus is LoadingStatus
            datastatus.data?.let { data.value = it }
        })
    }
    
    fun onDragStarted(dragTouch: Position): ControllerDragEvent? {
        return id?.let { id ->
            ControllerDragEvent(
                id = id,
                dragInfo = CommonDragInfo(
                    status = DRAG_START,
                    dragTouch = dragTouch,
                    from = GRAPH
                )).also(eventBus::addEvent)
        }
    }
}