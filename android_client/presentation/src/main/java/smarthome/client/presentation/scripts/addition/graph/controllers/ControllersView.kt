package smarthome.client.presentation.scripts.addition.graph.controllers

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.widget.FrameLayout
import androidx.lifecycle.observe
import com.snakydesign.livedataextensions.distinct
import org.koin.core.KoinComponent
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate
import smarthome.client.presentation.util.lifecycleOwner
import smarthome.client.util.log

class ControllersView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), KoinComponent {
    
    private val viewModel = ControllersViewViewModel() // todo scoped inject
    
    init {
        inflate(R.layout.scripts_controllers_to_add)
    
        setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewModel.onActionDown(event.rawX, event.x)
                }
                MotionEvent.ACTION_MOVE -> {
                    viewModel.moveTo(event.rawX)
                }
                MotionEvent.ACTION_UP -> {
                    viewModel.onActionUp()
                }
                else -> {
                    false
                }
            }
        }
    }
    
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        
        log("on layout")
        val lifecycle = lifecycleOwner ?: return
        
        viewModel.setWidth(width.toFloat())
        
        viewModel.jumpTo.observe(lifecycle) {
            animate().translationX(it).duration = 0
        }
        viewModel.dragging.distinct().observe(lifecycle) {
            animate().translationX(it).duration = 0
        }
        viewModel.animateTo.observe(lifecycle) {
            animate().translationX(it).duration = 300
        }
        
        viewModel.onInit()
    }
    
    fun open() {
        viewModel.open()
    }
}

