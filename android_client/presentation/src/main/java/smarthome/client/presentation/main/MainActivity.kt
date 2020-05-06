package smarthome.client.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import smarthome.client.presentation.R
import smarthome.client.presentation.SHOW_BOTTOM_BAR
import smarthome.client.presentation.SHOW_TOOL_BAR
import smarthome.client.presentation.core.BaseActivity
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.main.toolbar.ToolbarSetter
import smarthome.client.util.visible


class MainActivity : BaseActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val toolbarController: ToolbarController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        get<ToolbarSetter> { parametersOf(this, toolbar) }

        lifecycle.addObserver(viewModel)

        val navController = findNavController(R.id.nav_host_fragment)
        bottom_navigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, _, args ->
            toolbarController.clearMenu()

            bottom_navigation.visible = args?.getBoolean(SHOW_BOTTOM_BAR) ?: false
            toolbar_wrapper.visible = args?.getBoolean(SHOW_TOOL_BAR) ?: false

            toolbar.setNavigationOnClickListener { navController.navigateUp() }
        }

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}
