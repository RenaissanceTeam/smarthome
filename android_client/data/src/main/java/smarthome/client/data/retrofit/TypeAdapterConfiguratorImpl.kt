package smarthome.client.data.retrofit

import com.google.gson.GsonBuilder
import smarthome.client.data.retrofit.adapters.BlockTypeAdapterFactory

class TypeAdapterConfiguratorImpl(
    private val blockTypeAdapterFactory: BlockTypeAdapterFactory
) : TypeAdapterConfigurator {
    override fun configure(builder: GsonBuilder): GsonBuilder {
        return builder
            .registerTypeAdapterFactory(blockTypeAdapterFactory.factory)
        
    }
}