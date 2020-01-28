package smarthome.client.presentation.scripts.addition.graph.events

import io.reactivex.Observable

interface GraphEventBus {
    fun addEvent(event: GraphEvent)
    fun observe(): Observable<GraphEvent>
}

