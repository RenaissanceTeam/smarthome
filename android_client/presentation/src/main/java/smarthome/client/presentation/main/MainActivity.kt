package smarthome.client.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import smarthome.client.presentation.*


class MainActivity : FragmentActivity() {
    private val viewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        viewModel.openHomeServerSetup.observe(this) { navigateToHomeServerSelection() }
        viewModel.openLogin.observe(this) { navigateToLogin() }
        
        lifecycle.addObserver(viewModel)
        
        val navController = findNavController(R.id.nav_host_fragment)
        bottom_navigation.setupWithNavController(navController)
        
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        
        navController.addOnDestinationChangedListener { _, _, args ->
            bottom_navigation.visible = args?.getBoolean(
                SHOW_BOTTOM_BAR) ?: false
            toolbar.visible = args?.getBoolean(SHOW_TOOL_BAR) ?: false
        }
    }
    
    private fun navigateToHomeServerSelection() {
        nav_host_fragment.findNavController().navigate(
            R.id.action_global_homeServerFragment)
    }
    
    private fun navigateToLogin() {
        nav_host_fragment.findNavController().navigate(
            R.id.action_global_homeServerFragment)
    }
    
}
