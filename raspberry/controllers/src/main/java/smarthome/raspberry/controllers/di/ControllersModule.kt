package smarthome.raspberry.controllers.di

import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.raspberry.controllers.api.domain.GetControllerByIdUseCase
import smarthome.raspberry.controllers.api.domain.OnControllerChangedWithoutUserRequestUseCase
import smarthome.raspberry.controllers.api.domain.ReadControllerUseCase
import smarthome.raspberry.controllers.api.domain.WriteControllerUseCase
import smarthome.raspberry.controllers.domain.GetControllerByIdUseCaseImpl
import smarthome.raspberry.controllers.domain.OnControllerChangedWithoutUserRequestUseCaseImpl
import smarthome.raspberry.controllers.domain.ReadControllerUseCaseImpl
import smarthome.raspberry.controllers.domain.WriteControllerUseCaseImpl

private val domain = module {
    factoryBy<GetControllerByIdUseCase, GetControllerByIdUseCaseImpl>()
    factoryBy<OnControllerChangedWithoutUserRequestUseCase, OnControllerChangedWithoutUserRequestUseCaseImpl>()
    factoryBy<ReadControllerUseCase, ReadControllerUseCaseImpl>()
    factoryBy<WriteControllerUseCase, WriteControllerUseCaseImpl>()
}

val controllersModule = listOf(domain)
