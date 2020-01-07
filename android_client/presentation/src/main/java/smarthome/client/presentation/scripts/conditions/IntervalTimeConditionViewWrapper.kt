//package smarthome.client.presentation.screens.scripts.conditions
//
//import android.view.View
//import android.view.ViewGroup
//import android.widget.SeekBar
//import android.widget.TextView
//import smarthome.client.presentation.CONDITION_INTERVAL_TIME
//import smarthome.client.presentation.R
//
//
//class IntervalTimeConditionViewWrapper(private val condition: IntervalTimeCondition) : ConditionViewWrapper {
//
//    private var intervalView: TextView? = null
//    private var intervalSeek: SeekBar? = null
//
//    override fun getTag() = CONDITION_INTERVAL_TIME
//
//    override fun getView(root: ViewGroup): View {
//        val view = inflateLayout(root, R.layout.field_interval_time)
//
//        intervalView = view.findViewById(R.id.interval_value)
//        intervalSeek = view.findViewById(R.id.interval_seekbar)
//        intervalSeek?.max = 24 * 60
//
//        intervalSeek?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, newInterval: Int, fromUser: Boolean) {
//                if (!fromUser) return
//                condition.interval = newInterval
//                invalidateIntervalView()
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
//        })
//
//        invalidateIntervalView()
//        intervalSeek?.progress = condition.interval
//
//        return view
//    }
//
//    override fun isFilled() = true
//
//    private fun invalidateIntervalView() {
//        if (condition.interval < condition.min) {
//            condition.interval = condition.min
//            intervalSeek?.progress = condition.interval
//        }
//        intervalView?.text = condition.formatInterval()
//    }
//
//    override fun toString() = condition.toString()
//}