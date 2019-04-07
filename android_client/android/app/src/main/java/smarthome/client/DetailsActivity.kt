package smarthome.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import smarthome.client.fragments.controllerdetail.ControllerDetails
import smarthome.client.fragments.devicedetail.DeviceDetails
import smarthome.client.fragments.scriptdetail.ScriptDetails

class DetailsActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        replaceFragment(intent.extras, addToBackstack = false)
    }

    fun replaceFragment(arguments: Bundle?, addToBackstack: Boolean = true) {
        if (arguments == null) {
            finish()
            return
        }

        var fragment: Fragment? = null
        var fragmentTag = ""

        if (arguments.containsKey(DEVICE_GUID)) {
            fragment = DeviceDetails()
            fragmentTag = DeviceDetails.FRAGMENT_TAG
        } else if (arguments.containsKey(CONTROLLER_GUID)) {
            fragment = ControllerDetails()
            fragmentTag = ControllerDetails.FRAGMENT_TAG
        } else if (arguments.containsKey(SCRIPT_GUID)) {
            fragment = ScriptDetails()
            fragmentTag = ScriptDetails.FRAGMENT_TAG
        }

        if (fragment == null) {
            finish()
            return
        }

        fragment.arguments = arguments
        supportFragmentManager.apply {
            val transaction = beginTransaction().replace(R.id.root_view, fragment, fragmentTag)
            if (addToBackstack) transaction.addToBackStack(null)
            transaction.commit()
        }
    }

}