package smarthome.client.presentation.core

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import smarthome.client.presentation.R

abstract class BaseActivity : FragmentActivity() {
    
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            ?.childFragmentManager?.fragments?.firstOrNull()
        
        if (fragment is BaseFragment) {
            passBackPressToFragment(fragment)
            return
        }
        
        super.onBackPressed()
    }
    
    private fun passBackPressToFragment(fragment: BaseFragment) {
        lifecycleScope.launchWhenResumed {
            if (fragment.onBackPressed() == BackPressState.PROPAGATE) {
                super.onBackPressed()
            }
        }
    }
}
