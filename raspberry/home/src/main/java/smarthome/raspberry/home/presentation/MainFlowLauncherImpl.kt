package smarthome.raspberry.home.presentation

import android.content.Intent
import smarthome.raspberry.home.api.presentation.MainFlowLauncher
import smarthome.raspberry.home.presentation.main.MainActivity
import smarthome.raspberry.util.router.Router

class MainFlowLauncherImpl(
        private val router: Router
) : MainFlowLauncher {

    override fun launch() {
        router.startFlow(MainActivity::class,
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }
}