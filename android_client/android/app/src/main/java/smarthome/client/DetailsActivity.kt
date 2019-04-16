package smarthome.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import smarthome.client.fragments.controllerdetail.ControllerDetails
import smarthome.client.fragments.devicedetail.DeviceDetails
import smarthome.client.fragments.scriptdetail.ScriptDetails
import smarthome.client.fragments.scriptdetail.action.ActionFragment
import smarthome.client.fragments.scriptdetail.condition.ConditionFragment

class DetailsActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        replaceFragmentFromIntent(addToBackstack = false)
    }

    fun openConditionFragment() {
        replaceFragment(ConditionFragment())
    }

    fun openActionFragment() {
        replaceFragment(ActionFragment())
    }

    fun openControllerDetails(guid: Long) {
        val bundle = Bundle()
        bundle.putLong(CONTROLLER_GUID, guid)
        replaceFragment(ControllerDetails(), bundle)
    }

    private fun replaceFragmentFromIntent(addToBackstack: Boolean = true) {

        try {
            val fragment = chooseFragment()
            replaceFragment(fragment, arguments = intent.extras, addToBackstack = addToBackstack)
        } catch (e: Throwable) {
            finish()
        }
    }

    private fun chooseFragment(): Fragment {
        val fromExtras = intent.extras?.let { chooseFragment(it) }
        if (fromExtras != null) return fromExtras

        val fromAction = intent.action?.let { chooseFragment(it) }
        if (fromAction != null) return fromAction

        throw RuntimeException("No fragment found")
    }

    private fun chooseFragment(arguments: Bundle): Fragment? {
        if (arguments.containsKey(DEVICE_GUID)) {
            return DeviceDetails()
        }
        if (arguments.containsKey(CONTROLLER_GUID)) {
            return ControllerDetails()
        }
        if (arguments.containsKey(SCRIPT_GUID)) {
            return ScriptDetails()
        }
        return null
    }

    private fun chooseFragment(action: String): Fragment? {
        return when (action) {
            OPEN_CONDITION_SCRIPT -> ConditionFragment()
            else -> null
        }
    }

    private fun replaceFragment(fragment: Fragment, arguments: Bundle? = null,
                                addToBackstack: Boolean = true) {
        fragment.arguments = arguments
        supportFragmentManager.apply {
            val transaction = beginTransaction().replace(R.id.root_view, fragment)
            if (addToBackstack) transaction.addToBackStack(null)
            transaction.commit()
        }
    }

}