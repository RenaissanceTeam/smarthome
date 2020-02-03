package smarthome.client.presentation.scripts.addition.graph.blockviews.controller

import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.entity.Controller
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BorderStatus
import smarthome.client.presentation.scripts.addition.graph.events.EventPublisher
import smarthome.client.presentation.scripts.addition.graph.events.GraphEvent
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.drag.CommonDragInfo
import smarthome.client.presentation.scripts.addition.graph.events.drag.ControllerDragEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.DRAG_START
import smarthome.client.presentation.scripts.addition.graph.events.drag.GRAPH
import smarthome.client.presentation.scripts.addition.graph.identifier.ControllerGraphBlockIdentifier
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.Position
import smarthome.client.util.LoadingStatus
import smarthome.client.util.data
import kotlin.properties.Delegates


class GraphControllerViewModel: KoinViewModel(), EventPublisher {
    
    val visible = MutableLiveData<Boolean>()
    val dragVisible = MutableLiveData<Boolean>(true)
    val position = MutableLiveData<Position>()
    val loading = MutableLiveData<Boolean>()
    val data = MutableLiveData<Controller>()
    val border = MutableLiveData<BorderStatus>()
    val blockId = MutableLiveData<ControllerGraphBlockIdentifier>()
    
    private val eventBus: GraphEventBus by inject()
    private val observeController: ObserveControllerUseCase by inject()
    private var id by Delegates.observable<Long?>(null) { _, oldId, newId ->
        if (oldId != newId && newId != null) {
            observeController(newId)
            blockId.value = ControllerGraphBlockIdentifier(newId)
        }
    }
    
    override fun publish(e: GraphEvent) = eventBus.addEvent(e)
    
    fun onNewBlockData(block: ControllerBlock) {
        id = block.id.id
        visible.value = block.visible
        position.value = block.position
        border.value = block.border
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
                )
            ).also(eventBus::addEvent)
        }
    }
}