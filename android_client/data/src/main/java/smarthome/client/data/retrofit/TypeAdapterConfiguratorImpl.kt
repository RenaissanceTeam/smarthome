package smarthome.client.data.retrofit

import com.google.gson.GsonBuilder
import smarthome.client.data.retrofit.adapters.ActionTypeAdapterFactoryImpl
import smarthome.client.data.retrofit.adapters.BlockTypeAdapterFactoryImpl
import smarthome.client.data.retrofit.adapters.ConditionTypeAdapterFactoryImpl

class TypeAdapterConfiguratorImpl(
    private val blockTypeAdapterFactoryImpl: BlockTypeAdapterFactoryImpl,
    private val conditionTypeAdapterFactoryImpl: ConditionTypeAdapterFactoryImpl,
    private val actionTypeAdapterFactoryImpl: ActionTypeAdapterFactoryImpl
) : TypeAdapterConfigurator {
    override fun configure(builder: GsonBuilder): GsonBuilder {
        return builder
            .registerTypeAdapterFactory(blockTypeAdapterFactoryImpl.factory)
            .registerTypeAdapterFactory(conditionTypeAdapterFactoryImpl.factory)
            .registerTypeAdapterFactory(actionTypeAdapterFactoryImpl.factory)
    }
}