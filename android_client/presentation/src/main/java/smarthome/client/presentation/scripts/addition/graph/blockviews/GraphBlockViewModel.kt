package smarthome.client.presentation.scripts.addition.graph.blockviews

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import smarthome.client.entity.script.block.BlockId
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BorderStatus
import smarthome.client.presentation.scripts.addition.graph.events.EventPublisher
import smarthome.client.presentation.scripts.addition.graph.events.GraphEvent
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.util.Position


class GraphBlockViewModel : KoinViewModel(), EventPublisher {
    
    val visible = MutableLiveData<Boolean>()
    val dragVisible = MutableLiveData<Boolean>(true)
    val position = MutableLiveData<Position>()
    val loading = MutableLiveData<Boolean>()
    val border = MutableLiveData<BorderStatus>()
    val blockId = MutableLiveData<BlockId>()
    
    private val eventBus: GraphEventBus by inject()
    
    override fun publish(e: GraphEvent) = eventBus.addEvent(e)
    
    fun onNewBlockData(blockState: BlockState) {
        blockId.value = blockState.block.id
        visible.value = blockState.visible
        position.value = blockState.block.position
        border.value = blockState.border
    }
    
    fun setLoading(loading: Boolean) {
        this.loading.value = loading
    }
}