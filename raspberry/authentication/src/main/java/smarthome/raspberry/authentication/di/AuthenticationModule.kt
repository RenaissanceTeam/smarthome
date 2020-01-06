package smarthome.raspberry.authentication.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.raspberry.authentication.api.flow.SignInFlowLauncher
import smarthome.raspberry.authentication.flow.SignInFlowLauncherImpl

private val domain = module {

}

private val flow = module {
    factoryBy<SignInFlowLauncher, SignInFlowLauncherImpl>()
}

private val data = module {
}

val authenticationModule: List<Module> = listOf(
        domain,
        data,
        flow
)