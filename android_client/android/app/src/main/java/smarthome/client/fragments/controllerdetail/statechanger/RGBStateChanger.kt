package smarthome.client.fragments.controllerdetail.statechanger

import android.view.ViewGroup
import android.widget.SeekBar
import com.dd.processbutton.iml.ActionProcessButton
import smarthome.client.R

class RGBStateChanger(container: ViewGroup,
                      stateListener: (String) -> Unit,
                      writeListener: (String) -> Unit,
                      readListener: () -> Unit):
        ControllerStateChanger(container) {

    private val normalProgress = 0
    private val loadingProgress = 50
    private val unknownState = "Unknown"

    override val layout: Int
        get() = R.layout.state_changer_rgb

    private var currentState = ""
    private val r = rootView.findViewById<SeekBar>(R.id.state_changer_rgb_r_seek_bar)
    private val g = rootView.findViewById<SeekBar>(R.id.state_changer_rgb_g_seek_bar)
    private val b = rootView.findViewById<SeekBar>(R.id.state_changer_rgb_b_seek_bar)
    private val writeButton = rootView.findViewById<ActionProcessButton>(R.id.write_button)
    private val readButton = rootView.findViewById<ActionProcessButton>(R.id.read_button)
    private var pressedButton = writeButton

    private val step: Float = 2.5f

    private var rgb: Array<Int> = arrayOf(0, 0, 0)
    private var rgbS: String = ""
        get() = "${rgb[0]} ${rgb[1]} ${rgb[2]}"

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


    override fun invalidateNewState(state: String?, serveState: String?) {
        currentState = state ?: unknownState
        if (serveState == "up to date" || serveState == null) pressedButton.progress = normalProgress
        else pressedButton.progress = loadingProgress

    }

}