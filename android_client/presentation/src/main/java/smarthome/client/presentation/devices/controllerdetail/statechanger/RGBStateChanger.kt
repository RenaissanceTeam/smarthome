package smarthome.client.presentation.devices.controllerdetail.statechanger

import android.view.ViewGroup
import android.widget.SeekBar
import com.dd.processbutton.iml.ActionProcessButton
import smarthome.client.presentation.R
import java.lang.NumberFormatException

class RGBStateChanger(container: ViewGroup,
                      currState: String,
                      stateListener: (String) -> Unit,
                      writeListener: (String) -> Unit):
        ControllerStateChanger(container) {

    private val normalProgress = 0
    private val loadingProgress = 50
    private val unknownState = "Unknown"

    override val layout: Int
        get() = R.layout.state_changer_rgb

    private var currentState = currState
    private val r = rootView.findViewById<SeekBar>(R.id.state_changer_rgb_r_seek_bar)
    private val g = rootView.findViewById<SeekBar>(R.id.state_changer_rgb_g_seek_bar)
    private val b = rootView.findViewById<SeekBar>(R.id.state_changer_rgb_b_seek_bar)
    private val writeButton = rootView.findViewById<ActionProcessButton>(R.id.write_button)

    private val step: Float = 2.55f

    private var rgb: Array<Int> = arrayOf(0, 0, 0)
    private val rgbS: String
        get() = "${rgb[0]} ${rgb[1]} ${rgb[2]}"

    init {
        parseCurrentState()
        writeButton.normalText = WRITE_DEFAULT_TITLE
        writeButton.setOnClickListener {
            writeListener(currentState)
        }
        r.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rgb[0] = (progress * step).toInt()
                currentState = rgbS
                stateListener(rgbS)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }

        })
        g.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rgb[1] = (progress * step).toInt()
                currentState = rgbS
                stateListener(rgbS)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }

        })
        b.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rgb[2] = (progress * step).toInt()
                currentState = rgbS
                stateListener(rgbS)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }

        })
    }

    private fun parseCurrentState() {
        if (currentState.trim() == "")
            return

        val args = currentState.split(" ")

        var rValue = 0
        var gValue = 0
        var bValue = 0

        try {
            rValue = args[0].toInt()
            gValue = args[1].toInt()
            bValue = args[2].toInt()
        } catch (e: NumberFormatException) { }

        rgb[0] = rValue
        rgb[1] = gValue
        rgb[2] = bValue

        r.progress = (rValue / step).toInt()
        g.progress = (gValue / step).toInt()
        b.progress = (bValue / step).toInt()
    }

    override fun invalidateNewState(state: String?, serveState: String?) {
        currentState = state ?: unknownState
        if (serveState == "up to date" || serveState == null) writeButton.progress = normalProgress
        else writeButton.progress = loadingProgress

    }

}