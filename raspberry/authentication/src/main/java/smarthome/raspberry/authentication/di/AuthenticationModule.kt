package smarthome.raspberry.authentication.di

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.library.datalibrary.api.boundary.UserIdHolder
import smarthome.raspberry.authentication.api.domain.GetAuthStatusUseCase
import smarthome.raspberry.authentication.api.domain.GetUserIdUseCase
import smarthome.raspberry.authentication.api.domain.GetUserInfoUseCase
import smarthome.raspberry.authentication.api.flow.SignInFlowLauncher
import smarthome.raspberry.authentication.data.AuthRepo
import smarthome.raspberry.authentication.data.AuthRepoImpl
import smarthome.raspberry.authentication.data.UserIdHolderImpl
import smarthome.raspberry.authentication.data.command.SignInCommand
import smarthome.raspberry.authentication.data.command.SignInCommandImpl
import smarthome.raspberry.authentication.data.command.SignOutCommand
import smarthome.raspberry.authentication.data.command.SignOutCommandImpl
import smarthome.raspberry.authentication.data.query.GetUserQuery
import smarthome.raspberry.authentication.data.query.GetUserQueryImpl
import smarthome.raspberry.authentication.domain.AuthenticationStateResolver
import smarthome.raspberry.authentication.domain.GetAuthStatusUseCaseImpl
import smarthome.raspberry.authentication.domain.GetUserIdUseCaseImpl
import smarthome.raspberry.authentication.domain.GetUserInfoUseCaseImpl
import smarthome.raspberry.authentication.flow.SignInFlowLauncherImpl
import smarthome.raspberry.util.ResourceProvider

private val domain = module {
    factoryBy<GetAuthStatusUseCase, GetAuthStatusUseCaseImpl>()
    factoryBy<GetUserIdUseCase, GetUserIdUseCaseImpl>()
    factoryBy<GetUserInfoUseCase, GetUserInfoUseCaseImpl>()
    single {
        AuthenticationStateResolver(
            repo = get(),
            publishEventUseCase = get(),
            signInFlowLauncher = get(),
            getHomeEventsUseCase = get()
        )
    }
}

private val flow = module {
    factoryBy<SignInFlowLauncher, SignInFlowLauncherImpl>()
}

private val data = module {
    singleBy<AuthRepo, AuthRepoImpl>()
    singleBy<UserIdHolder, UserIdHolderImpl>()
    factoryBy<SignInCommand, SignInCommandImpl>()
    factoryBy<SignOutCommand, SignOutCommandImpl>()
    factoryBy<GetUserQuery, GetUserQueryImpl>()
}

val authenticationModule: List<Module> = listOf(
        domain,
        data,
        flow
)