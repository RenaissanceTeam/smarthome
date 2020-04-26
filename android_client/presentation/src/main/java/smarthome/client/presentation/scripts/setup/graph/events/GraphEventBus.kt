package smarthome.client.presentation.scripts.setup.graph.events

import io.reactivex.Observable

interface GraphEventBus {
    fun addEvent(event: GraphEvent)
    fun observe(): Observable<GraphEvent>
}

