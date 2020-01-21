package smarthome.client.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import smarthome.client.presentation.R
import smarthome.client.presentation.SHOW_BOTTOM_BAR
import smarthome.client.presentation.SHOW_TOOL_BAR
import smarthome.client.presentation.visible


class MainActivity : FragmentActivity() {
    private val viewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    
        viewModel.openHomeServerSetup.onNavigate(this, ::navigateToHomeServerSelection)
        viewModel.openLogin.onNavigate(this, ::navigateToLogin)
        
        lifecycle.addObserver(viewModel)
        
        val navController = findNavController(R.id.nav_host_fragment)
        bottom_navigation.setupWithNavController(navController)
        
        
        navController.addOnDestinationChangedListener { _, _, args ->
            bottom_navigation.visible = args?.getBoolean(SHOW_BOTTOM_BAR) ?: false
            toolbar.visible = args?.getBoolean(SHOW_TOOL_BAR) ?: false
        }
        
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }
    
    private fun navigateToHomeServerSelection() {
        nav_host_fragment.findNavController().navigate(R.id.action_global_homeServerFragment)
    }
    
    private fun navigateToLogin() {
        nav_host_fragment.findNavController().navigate(R.id.action_global_loginFragment)
    }
    
}
