package smarthome.client.presentation.main.toolbar

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class ToolbarSetter(owner: LifecycleOwner,
                    private val toolbar: Toolbar,
                    private val holder: ToolbarHolder
) : LifecycleObserver {
    init {
        owner.lifecycle.addObserver(this)
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        holder.toolbar = toolbar
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        holder.toolbar = null
    }
}