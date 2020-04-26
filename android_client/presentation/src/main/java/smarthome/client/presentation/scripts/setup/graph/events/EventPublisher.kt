package smarthome.client.presentation.scripts.setup.graph.events

interface EventPublisher {
    fun publish(e: GraphEvent)
}