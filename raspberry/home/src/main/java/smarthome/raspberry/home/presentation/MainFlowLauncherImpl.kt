package smarthome.raspberry.home.presentation

import android.content.Context
import android.content.Intent
import smarthome.raspberry.home.api.presentation.MainFlowLauncher
import smarthome.raspberry.home.presentation.main.MainActivity

class MainFlowLauncherImpl(
        private val context: Context
) : MainFlowLauncher {

    override fun launch() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }
}