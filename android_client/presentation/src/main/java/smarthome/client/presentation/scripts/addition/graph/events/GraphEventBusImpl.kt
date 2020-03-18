package smarthome.client.presentation.scripts.addition.graph.events

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import smarthome.client.util.log

class GraphEventBusImpl : GraphEventBus {
    private val events = PublishSubject.create<GraphEvent>()
    
    override fun addEvent(event: GraphEvent) {
        events.onNext(event)
    }
    
    override fun observe(): Observable<GraphEvent> = events
}