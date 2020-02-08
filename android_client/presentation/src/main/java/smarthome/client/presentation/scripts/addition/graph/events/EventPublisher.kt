package smarthome.client.presentation.scripts.addition.graph.events

interface EventPublisher {
    fun publish(e: GraphEvent)
}