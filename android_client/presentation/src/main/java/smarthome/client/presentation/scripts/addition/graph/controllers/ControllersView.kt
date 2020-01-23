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
    private var velocityTracker: VelocityTracker? = null
    
    init {
        inflate(R.layout.scripts_controllers_to_add)
    
        setOnClickListener { viewModel.onClick() }
//
//        val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
//
//            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float,
//                                 velocityY: Float): Boolean {
//                if (velocityX > 0) log("on fling")
//                return super.onFling(e1, e2, velocityX, velocityY)
//            }
//
//            override fun onScroll(start: MotionEvent, current: MotionEvent, distanceX: Float,
//                                  distanceY: Float): Boolean {
//                if (start.x / width > LEFT_SIDE_PERCENT) return true
//                animate().translationX(current.rawX - start.rawX).setDuration(0L)
//                return super.onScroll(start, current, distanceX, distanceY)
//            }
//        })
    
    
        setOnTouchListener { v, event ->
            //            gestureDetector.onTouchEvent(event)
//            true
        
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    velocityTracker = VelocityTracker.obtain()
                    velocityTracker?.addMovement(event)
                    return@setOnTouchListener viewModel.onActionDown(event.rawX, event.x, width.toFloat())
                }
                MotionEvent.ACTION_MOVE -> {
                    velocityTracker?.addMovement(event)
                
                    velocityTracker?.computeCurrentVelocity(PIXELS_PER_SECOND)
                    log("velocity = ${velocityTracker?.xVelocity}")
                    return@setOnTouchListener viewModel.moveTo(event.rawX)
                }
            
                MotionEvent.ACTION_UP -> {
                    velocityTracker?.recycle()
                    velocityTracker = null
                }
                else -> {
                }
            }
        
            true
        }
    }
    
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        
        log("on layout")
        val lifecycle = lifecycleOwner ?: return
        
        viewModel.setWidth(width.toFloat())
        viewModel.isOpen.observe(lifecycle) { isOpen ->
            log("is open $isOpen")
            when (isOpen) {
                true -> animate().translationX(0f).setDuration(300)
                false -> {
                    val toFloat = width.toFloat()
                    log("translate x to $toFloat")
                    animate().translationX(toFloat).setDuration(0)
                }
            }
        }
        
        viewModel.dragging.distinct().observe(lifecycle) {
            animate().translationX(it).setDuration(0)
        }
    }
    
    fun open() {
        viewModel.open()
    }
    
    companion object {
        const val PIXELS_PER_SECOND = 1000
    }
}

