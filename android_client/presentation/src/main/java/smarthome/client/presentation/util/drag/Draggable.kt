package smarthome.client.presentation.util.drag

import android.view.View
import smarthome.client.util.Position

interface Draggable {
    var host: DraggableHost?
    var possibleHosts: () -> List<DraggableHost>
    var stableRawPosition: Position
    var currentRawPosition: Position
    var touchPosition: Position
    
    fun setCurrentPosition(position: Position) {
        currentRawPosition = position - touchPosition
    }
    
    fun moveUiToCurrent() {
        host?.convertRawToRelativePosition(currentRawPosition)?.let(::doUiMove)
    }
    
    
    fun setTouchHandler(view: View)
    
    fun doUiMove(position: Position)
}

