package smarthome.client.presentation.fragments.deviceaddition


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

import smarthome.client.R
import smarthome.client.util.CloudStorages
import smarthome.library.common.message.Message

val DISCOVER_REQUEST_CODE = 2356

const val SEARCH_ALL_METHOD = "Search all"
const val SEARCH_GATEWAY_METHOD = "Xiaomi Gateway"

val dataSource: List<Pair<String, Int>> = listOf(
        Pair(SEARCH_ALL_METHOD, R.drawable.any),
        Pair(SEARCH_GATEWAY_METHOD, R.drawable.xiaomi_gateway_2)
)

class DeviceSelectorFragment : Fragment() {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private var recycler: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_device_selector, container, false)

        recycler = view.findViewById(R.id.device_selector_recycler)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler?.layoutManager = GridLayoutManager(context, 2)
        val adapter = DeviceSelectorAdapter(dataSource, ::processSearchMethod)
        recycler?.adapter = adapter
    }

    fun processSearchMethod(method: String, args: String?) {
        sendDiscoverRequest(MessageFactory.createMessage(method, args))
        activity?.setResult(DISCOVER_REQUEST_CODE)
        activity?.finish()
    }

    private fun sendDiscoverRequest(message: Message) {
        uiScope.launch {
            CloudStorages.getMessageQueue()
                    .postMessage(message)
        }
    }

}
