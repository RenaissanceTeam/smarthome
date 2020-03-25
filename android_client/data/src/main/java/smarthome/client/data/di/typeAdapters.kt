package smarthome.client.data.di

import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import smarthome.client.data.api.typeadapter.DataTypeAdapter
import smarthome.client.data.retrofit.TypeAdapterConfigurator
import smarthome.client.data.retrofit.TypeAdapterConfiguratorImpl
import smarthome.client.data.retrofit.adapters.BlockTypeAdapterFactory
import smarthome.client.entity.script.block.Block

val typeAdapterModule = module {
    singleBy<TypeAdapterConfigurator, TypeAdapterConfiguratorImpl>()
    
    single { BlockTypeAdapterFactory() }
    single<DataTypeAdapter<Block>> { get<BlockTypeAdapterFactory>() }
    
    
}
