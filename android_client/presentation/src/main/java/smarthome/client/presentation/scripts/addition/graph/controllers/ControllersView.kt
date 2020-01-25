package smarthome.client.presentation.scripts.addition.graph.controllers

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.snakydesign.livedataextensions.distinct
import kotlinx.android.synthetic.main.scripts_controllers_to_add.view.*
import org.koin.core.KoinComponent
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.graph.controllers.epoxy.DevicesController
import smarthome.client.presentation.util.inflate
import smarthome.client.presentation.util.lifecycleOwner

class ControllersView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), KoinComponent {
    
    private val viewModel = ControllersViewViewModel() // todo scoped inject
    private val itemsController = DevicesController()
    
    init {
        inflate(R.layout.scripts_controllers_to_add)
    
        devices.layoutManager = LinearLayoutManager(context)
        devices.adapter = itemsController.adapter
    
        setupTouchListener()
        onLayoutReady()
    }
    
    private fun onLayoutReady() {
        post {
            viewModel.setWidth(width.toFloat())
            viewModel.onInit()
        }
    }
    
    private fun setupTouchListener() = setOnTouchListener { _, event ->
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
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        
        val lifecycle = lifecycleOwner ?: return
        lifecycle.lifecycle.addObserver(viewModel)
        
        viewModel.jumpTo.distinct().observe(lifecycle) {
            animate().translationX(it).duration = 0
        }
        viewModel.animateTo.observe(lifecycle) {
            animate().translationX(it).duration = 300
        }
        viewModel.devices.observe(lifecycle) {
            itemsController.setData(it, viewModel)
        }
    }
    
    fun open() {
        viewModel.open()
    }
}

