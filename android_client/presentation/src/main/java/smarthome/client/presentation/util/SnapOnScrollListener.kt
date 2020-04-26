package smarthome.client.presentation.util

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

class SnapOnScrollListener(
    private val snapHelper: SnapHelper,
    var onSnapPositionChangeListener: (Int) -> Unit
) : RecyclerView.OnScrollListener() {
    private var snapPosition = RecyclerView.NO_POSITION
    
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            maybeNotifySnapPositionChange(recyclerView)
        }
    }
    
    private fun maybeNotifySnapPositionChange(recyclerView: RecyclerView) {
        val snapView = snapHelper.findSnapView(recyclerView.layoutManager)
        val snapPosition = recyclerView.layoutManager!!.getPosition(snapView!!)
        val snapPositionChanged = this.snapPosition != snapPosition
        if (snapPositionChanged) {
            onSnapPositionChangeListener(snapPosition)
            this.snapPosition = snapPosition
        }
    }
}
