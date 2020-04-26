package smarthome.client.presentation.util.drag

import android.view.View
import io.reactivex.Observable
import smarthome.client.util.Position

interface Draggable {
    var host: DraggableHost?
    var possibleHosts: () -> List<DraggableHost>
    var stableRawPosition: Position
    var currentRawPosition: Position
    val currentHostPosition: Position?
        get() = host?.convertRawToRelativePosition(currentRawPosition)
    var touchPosition: Position
    fun setCurrentPosition(position: Position) {
        currentRawPosition = position - touchPosition
    }
    fun observeEvents(): Observable<DraggableEvent>
    fun setTouchHandler(view: View)
}

