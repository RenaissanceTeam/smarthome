package smarthome.client

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import smarthome.client.fragments.devicedetail.DeviceDetails

class DetailsActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val deviceDetails = DeviceDetails()
        deviceDetails.arguments = intent.extras

        supportFragmentManager?.apply { beginTransaction().add(R.id.root_view, deviceDetails, DeviceDetails.FRAGMENT_TAG).commit() }
    }
}