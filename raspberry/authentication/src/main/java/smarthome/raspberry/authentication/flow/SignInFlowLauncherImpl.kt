package smarthome.raspberry.authentication.flow

import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.raspberry.authentication.api.flow.SignInFlowLauncher
import smarthome.raspberry.authentication.presentation.AuthenticationActivity
import smarthome.raspberry.util.router.Router

class SignInFlowLauncherImpl : SignInFlowLauncher, KoinComponent {
    private val router: Router by inject()
    
    override fun launch() {
        router.startFlow(AuthenticationActivity::class)
    }
}
