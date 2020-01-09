package smarthome.client.presentation

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
        
        viewModel.isAuthenticated.observe(this) { if (!it) navigateToLogin() }
        viewModel.hasHomeServer.observe(this) { if (!it) navigateToHomeServerSelection() }
        
        lifecycle.addObserver(viewModel)
        
        val navController = findNavController(R.id.nav_host_fragment)
        bottom_navigation.setupWithNavController(navController)
        
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { a, destination, c ->
            bottom_navigation.visible = destination.arguments.containsKey(SHOW_BOTTOM_BAR)
            toolbar.visible = destination.arguments.containsKey(SHOW_TOOL_BAR)
        }
    }
    
    private fun navigateToHomeServerSelection() {
        nav_host_fragment.findNavController().navigate(R.id.action_global_homeServerFragment)
    }
    
    private fun navigateToLogin() {
        nav_host_fragment.findNavController().navigate(R.id.action_global_homeServerFragment)
    
    }
    
}
