package smarthome.raspberry.authentication.di

import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.raspberry.authentication.api.domain.GetAuthStatusUseCase
import smarthome.raspberry.authentication.api.domain.GetUserIdUseCase
import smarthome.raspberry.authentication.api.domain.GetUserInfoUseCase
import smarthome.raspberry.authentication.api.flow.SignInFlowLauncher
import smarthome.raspberry.authentication.data.AuthRepo
import smarthome.raspberry.authentication.data.AuthRepoImpl
import smarthome.raspberry.authentication.data.mapper.FirebaseUserToUserMapper
import smarthome.raspberry.authentication.data.mapper.FirebaseUserToUserMapperImpl
import smarthome.raspberry.authentication.domain.GetAuthStatusUseCaseImpl
import smarthome.raspberry.authentication.domain.GetUserIdUseCaseImpl
import smarthome.raspberry.authentication.domain.GetUserInfoUseCaseImpl
import smarthome.raspberry.authentication.flow.SignInFlowLauncherImpl

private val domain = module {
    factoryBy<GetAuthStatusUseCase, GetAuthStatusUseCaseImpl>()
    factoryBy<GetUserIdUseCase, GetUserIdUseCaseImpl>()
    factoryBy<GetUserInfoUseCase, GetUserInfoUseCaseImpl>()
}

private val flow = module {
    factoryBy<SignInFlowLauncher, SignInFlowLauncherImpl>()
}

private val data = module {
    factoryBy<FirebaseUserToUserMapper, FirebaseUserToUserMapperImpl>()
    singleBy<AuthRepo, AuthRepoImpl>()
}

private val presentation = module {
    // todo - how to provide view inside presenter
}

val authenticationModule = listOf(
        presentation,
        domain,
        data,
        flow
)