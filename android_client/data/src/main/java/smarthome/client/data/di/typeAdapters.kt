package smarthome.client.data.di

import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import smarthome.client.data.api.typeadapter.DataTypeAdapter
import smarthome.client.data.retrofit.TypeAdapterConfigurator
import smarthome.client.data.retrofit.TypeAdapterConfiguratorImpl
import smarthome.client.data.retrofit.adapters.ActionTypeAdapterFactory
import smarthome.client.data.retrofit.adapters.BlockTypeAdapterFactoryImpl
import smarthome.client.data.retrofit.adapters.ConditionTypeAdapterFactory
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.action.ActionData
import smarthome.client.entity.script.dependency.condition.ConditionData

val typeAdapterModule = module {
    singleBy<TypeAdapterConfigurator, TypeAdapterConfiguratorImpl>()
    
    single { BlockTypeAdapterFactoryImpl() }
    single<DataTypeAdapter<Block>>() { get<BlockTypeAdapterFactoryImpl>() }
    
    single { ConditionTypeAdapterFactory() }
    single<DataTypeAdapter<ConditionData>> { get<ConditionTypeAdapterFactory>() }
    
    single { ActionTypeAdapterFactory() }
    single<DataTypeAdapter<ActionData>> { get<ActionTypeAdapterFactory>() }
}
