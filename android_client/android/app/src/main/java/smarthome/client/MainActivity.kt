package smarthome.client

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : FragmentActivity() {

    private val viewModel
            by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    private val bottomNavigation by lazy { findViewById<BottomNavigationView>(R.id.bottom_navigation) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.isAuthenticated.observe(this, Observer { if (!it) launchAuthActivity() })
        bottomNavigation.setupWithNavController(findNavController(R.id.nav_host_fragment))
    }

    private fun launchAuthActivity() {
        startActivityForResult(viewModel.authUiWrapper.getAuthIntent(), RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                viewModel.onAuthSuccessful()
            } else {
                viewModel.onAuthFailed()
            }
        }
    }
}
