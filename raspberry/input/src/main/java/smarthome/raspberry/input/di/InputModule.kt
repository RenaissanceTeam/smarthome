package smarthome.raspberry.input.di

import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.raspberry.input.api.domain.HandleInputByParsingChangedDevicesUseCase
import smarthome.raspberry.input.data.InputControllerDataSource
import smarthome.raspberry.input.domain.HandleInputByParsingChangedDevicesUseCaseImpl

private val domain = module {
    factoryBy<HandleInputByParsingChangedDevicesUseCase, HandleInputByParsingChangedDevicesUseCaseImpl>()
}

private val data = module {
}

val inputModule = listOf(
        domain,
        data
)