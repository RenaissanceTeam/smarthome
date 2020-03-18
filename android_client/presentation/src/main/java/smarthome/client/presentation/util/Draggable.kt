package smarthome.client.presentation.util

import android.view.MotionEvent
import android.view.View
import smarthome.client.presentation.util.extensions.position
import smarthome.client.util.Position

interface Draggable {
    
    var host: DraggableHost
    var possibleHosts: () -> List<DraggableHost>
    var stablePosition: Position
    var currentPosition: Position
//    var touchPosition: Position
    
    fun setTouchHandler(view: View) {
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
//                    val touchX = (view.x + event.x).toInt()
//                    val touchY = (view.y + event.y).toInt()
                    
                    host.onStartedDragging(this)
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    currentPosition = event.position
                    host.onMovedDraggable(this)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    when (val newHost = possibleHosts().find { it.hitTest(currentPosition) }) {
                        null -> {
                            currentPosition = stablePosition
                            host.onCancel(this, stablePosition)
                        }
                        host -> {
                            stablePosition = currentPosition
                            host.onFinishMovingDraggableInsideHost(this)
                        }
                        else -> {
                            stablePosition = currentPosition
                            host.onRemove(this)
                            newHost.onAdd(this)
                        }
                    }
                    true
                }
                else -> false
            }
            
        }
    }
    
    interface DraggableHost {
        val hosted: MutableList<Draggable>
        
        fun hitTest(position: Position): Boolean
        fun onStartedDragging(draggable: Draggable)
        fun onMovedDraggable(draggable: Draggable)
        fun onFinishMovingDraggableInsideHost(draggable: Draggable)
        
        fun onCancel(draggable: Draggable, initial: Position)
        
        fun onAdd(draggable: Draggable) {
            hosted.add(draggable)
        }
        
        fun onRemove(draggable: Draggable) {
            hosted.remove(draggable)
        }
    }