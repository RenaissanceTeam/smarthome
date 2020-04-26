package smarthome.client.presentation.util.drag

import smarthome.client.util.Position

interface DraggableHost {
    val hosted: MutableList<Draggable>
    
    fun hitTest(position: Position): Boolean
    fun convertRawToRelativePosition(raw: Position): Position
    
    fun onStartedDragging(draggable: Draggable)
    fun onMovedDraggable(draggable: Draggable)
    fun onFinishMovingDraggableInsideHost(draggable: Draggable)
    
    fun onCancel(draggable: Draggable, initial: Position)
    
    fun onAdd(draggable: Draggable) {
        hosted.add(draggable)
        draggable.host = this
    }
    
    fun onRemove(draggable: Draggable) {
        hosted.remove(draggable)
        draggable.host = null
    }
}