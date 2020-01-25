package smarthome.client.presentation.scripts.addition.graph

interface GraphDraggable {
    fun onDraggedToGraph()
    fun onDragCancelled()
    fun getBlock(): GraphBlock
    fun moveTo(position: Position)
}