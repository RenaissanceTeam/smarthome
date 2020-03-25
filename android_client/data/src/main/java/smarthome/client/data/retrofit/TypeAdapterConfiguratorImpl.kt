package smarthome.client.data.retrofit

import com.google.gson.GsonBuilder
import smarthome.client.data.retrofit.adapters.BlockTypeAdapterFactoryImpl

class TypeAdapterConfiguratorImpl(
    private val blockTypeAdapterFactoryImpl: BlockTypeAdapterFactoryImpl
) : TypeAdapterConfigurator {
    override fun configure(builder: GsonBuilder): GsonBuilder {
        return builder
            .registerTypeAdapterFactory(blockTypeAdapterFactoryImpl.factory)
        
    }
}