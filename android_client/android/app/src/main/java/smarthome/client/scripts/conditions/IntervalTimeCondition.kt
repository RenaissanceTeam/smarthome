package smarthome.client.scripts.conditions

import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import smarthome.client.CONDITION_EXACT_TIME
import smarthome.client.CONDITION_INTERVAL_TIME
import smarthome.client.R

class IntervalTimeCondition : Condition(), ConditionViewBuilder {
    private val min = 5

    var interval = min
    var intervalView: TextView? = null
    var intervalSeek: SeekBar? = null


    override fun getTag() = CONDITION_INTERVAL_TIME

    override fun getView(root: ViewGroup): View {
        val view = inflateLayout(root, R.layout.field_interval_time)

        intervalView = view.findViewById(R.id.interval_value)
        intervalSeek = view.findViewById(R.id.interval_seekbar)
        intervalSeek?.max = 24 * 60

        intervalSeek?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, newInterval: Int, fromUser: Boolean) {
                if (!fromUser) return
                interval = newInterval
                invalidateIntervalView()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        invalidateIntervalView()
        intervalSeek?.progress = interval

        return view
    }

    override fun isFilled() = true

    private fun invalidateIntervalView() {
        if (interval < min) {
            interval = min
            intervalSeek?.progress = interval
        }
        intervalView?.text = formatInterval()
    }

    private fun formatInterval(): String {
        val hours = interval / 60
        val minutes = interval % 60

        if (hours == 0) return "$minutes min"
        if (minutes == 0) return "$hours h"

        return "$hours h. $minutes min"
    }

    override fun toString() = "At interval ${formatInterval()}"

    override fun evaluate(): Boolean = true // todo
}