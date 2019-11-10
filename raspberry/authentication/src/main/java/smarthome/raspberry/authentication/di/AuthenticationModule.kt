package smarthome.raspberry.authentication.di

import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.raspberry.authentication.api.domain.GetAuthStatusUseCase
import smarthome.raspberry.authentication.domain.GetAuthStatusUseCaseImpl

val authenticationModule = module {
    factoryBy<GetAuthStatusUseCase, GetAuthStatusUseCaseImpl>()
}