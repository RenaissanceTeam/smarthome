package smarthome.client.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : FragmentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.isAuthenticated.observe(this) { if (!it) launchAuthActivity() }
        viewModel.hasHomeServer.observe(this) { if (!it) navigateToHomeServerSelection() }

        lifecycle.addObserver(viewModel)
        
        val navController = findNavController(R.id.nav_host_fragment)
        bottom_navigation.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { a, destination, c ->
            handleVisibility(bottom_navigation, destination.arguments.containsKey(SHOW_BOTTOM_BAR))
            handleVisibility(toolbar, destination.arguments.containsKey(SHOW_TOOL_BAR))
        }
    }

    private fun handleVisibility(view: View, show: Boolean) {
        if (show) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
    
    private fun navigateToHomeServerSelection() {
        nav_host_fragment.findNavController().navigate(R.id.action_global_homeServerFragment)
    }

    private fun launchAuthActivity() {
        TODO()
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
