package smarthome.client.presentation.controllers.controllerdetail.statechanger

import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import com.dd.processbutton.iml.ActionProcessButton
import smarthome.client.presentation.R

class DimmerStateChanger(container: ViewGroup,
                         stateTitle: TextView?,
                         stateListener: (String) -> Unit,
                         writeListener: (String) -> Unit,
                         readListener: () -> Unit,
                         upperBound: Int = 100) :
    ControllerStateChanger(container) {
    
    private val normalProgress = 0
    private val loadingProgress = 50
    private val unknownState = "Unknown"
    
    override val layout: Int
        get() = R.layout.state_changer_dimmer
    
    private var currentState = ""
    private val seekBar = rootView.findViewById<SeekBar>(R.id.state_changer_seekbar)
    private val writeButton = rootView.findViewById<ActionProcessButton>(R.id.write_button)
    private val readButton = rootView.findViewById<ActionProcessButton>(R.id.read_button)
    private var pressedButton = writeButton
    
    private val step = upperBound / 100
    
    init {
        readButton.setOnClickListener {
            pressedButton = readButton
            readListener()
        }
        writeButton.normalText = WRITE_DEFAULT_TITLE
        writeButton.setOnClickListener {
            pressedButton = writeButton
            writeListener(currentState)
        }
        val state = stateTitle?.text.toString()
        seekBar.progress = if (state == "") 0 else state.toInt()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val res = (progress * step).toString()
                currentState = res
                stateListener(res)
            }
            
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        stateTitle?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val state = s.toString()
                seekBar.progress = if (state == "") 0 else state.toInt() / step
            }
            
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
    
    override fun invalidateNewState(state: String) {
        currentState = state
    }
}