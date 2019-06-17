package smarthome.client.presentation.fragments.deviceaddition


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_device_selector.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import smarthome.client.R
import smarthome.library.common.message.Message

const val DISCOVER_REQUEST_CODE = 2356

const val SEARCH_ALL_METHOD = "Search all"
const val SEARCH_GATEWAY_METHOD = "Xiaomi Gateway"

val dataSource: List<Pair<String, Int>> = listOf(
        Pair(SEARCH_ALL_METHOD, R.drawable.any),
        Pair(SEARCH_GATEWAY_METHOD, R.drawable.xiaomi_gateway_2)
)

class DeviceSelectorFragment : Fragment() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        device_selector_recycler.layoutManager = GridLayoutManager(context, 2)
        device_selector_recycler.adapter = DeviceSelectorAdapter(dataSource, ::processSearchMethod)
    }

    private fun processSearchMethod(method: String, args: String?) {
        sendDiscoverRequest(MessageFactory.createMessage(method, args))
        activity?.setResult(DISCOVER_REQUEST_CODE)
        activity?.finish()
    }

    private fun sendDiscoverRequest(message: Message) {
        uiScope.launch {
//            CloudStorages.getMessageQueue()
//                    .postMessage(message)
        }
    }

}
