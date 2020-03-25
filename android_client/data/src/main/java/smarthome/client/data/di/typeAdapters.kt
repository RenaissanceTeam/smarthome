package smarthome.client.data.di

import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import smarthome.client.data.api.typeadapter.ActionDataTypeAdapter
import smarthome.client.data.api.typeadapter.BlockTypeAdapter
import smarthome.client.data.api.typeadapter.ConditionDataTypeAdapter
import smarthome.client.data.retrofit.TypeAdapterConfigurator
import smarthome.client.data.retrofit.TypeAdapterConfiguratorImpl
import smarthome.client.data.retrofit.adapters.ActionTypeAdapterFactoryImpl
import smarthome.client.data.retrofit.adapters.BlockTypeAdapterFactoryImpl
import smarthome.client.data.retrofit.adapters.ConditionTypeAdapterFactoryImpl

val typeAdapterModule = module {
    singleBy<TypeAdapterConfigurator, TypeAdapterConfiguratorImpl>()
    
    
    single { BlockTypeAdapterFactoryImpl() }
    single<BlockTypeAdapter> { get<BlockTypeAdapterFactoryImpl>() }
    
    single { ConditionTypeAdapterFactoryImpl() }
    single<ConditionDataTypeAdapter> { get<ConditionTypeAdapterFactoryImpl>() }
    
    single { ActionTypeAdapterFactoryImpl() }
    single<ActionDataTypeAdapter> { get<ActionTypeAdapterFactoryImpl>() }
}
