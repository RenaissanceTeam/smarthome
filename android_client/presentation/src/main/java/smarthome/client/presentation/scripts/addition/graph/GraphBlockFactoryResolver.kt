package smarthome.client.presentation.scripts.addition.graph

interface GraphBlockFactoryResolver {
    fun resolve(block: GraphBlock): GraphBlockFactory?
}