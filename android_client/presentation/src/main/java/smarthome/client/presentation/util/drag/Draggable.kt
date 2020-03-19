package smarthome.client.presentation.util.drag

import android.view.MotionEvent
import android.view.View
import smarthome.client.presentation.util.extensions.rawPosition
import smarthome.client.util.Position

interface Draggable {
    var host: DraggableHost?
    var possibleHosts: () -> List<DraggableHost>
    var stableRawPosition: Position
    var currentRawPosition: Position
    var touchPosition: Position
    
    private fun setCurrentPosition(position: Position) {
        currentRawPosition = position - touchPosition
    }
    
    private fun moveUiToCurrent() {
        host?.convertRawToRelativePosition(currentRawPosition)?.let(::doUiMove)
    }
    
    
    fun setTouchHandler(view: View) {
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val touchX = (view.x + event.x).toInt()
                    val touchY = (view.y + event.y).toInt()
                    touchPosition = Position(touchX, touchY)
    
    
                    setCurrentPosition(event.rawPosition)
                    host?.onStartedDragging(this)
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    setCurrentPosition(event.rawPosition)
    
                    host?.onMovedDraggable(this)
    
    
                    moveUiToCurrent()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    when (val newHost = possibleHosts().find { it.hitTest(currentRawPosition) }) {
                        null -> {
                            currentRawPosition = stableRawPosition
                            host?.onCancel(this, stableRawPosition)
                            moveUiToCurrent()
                        }
                        host -> {
                            stableRawPosition = currentRawPosition
                            host?.onFinishMovingDraggableInsideHost(this)
                        }
                        else -> {
                            stableRawPosition = currentRawPosition
                            host?.onRemove(this)
                            newHost.onAdd(this)
                        }
                    }
                    true
                }
                else -> false
            }
            
        }
    }
    
    fun doUiMove(position: Position)
}

