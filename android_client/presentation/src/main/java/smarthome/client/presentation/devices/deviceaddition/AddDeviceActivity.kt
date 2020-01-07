package smarthome.client.presentation.devices.deviceaddition

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import smarthome.client.presentation.R

class AddDeviceActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_device)

        supportFragmentManager.apply {
            val transaction = beginTransaction().replace(
                R.id.add_device_activity_container, DeviceSelectorFragment())
            transaction.commit()
        }
    }
}
