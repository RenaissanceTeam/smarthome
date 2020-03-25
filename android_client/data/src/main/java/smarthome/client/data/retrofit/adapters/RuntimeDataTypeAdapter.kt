package smarthome.client.data.retrofit.adapters

import smarthome.client.data.api.typeadapter.DataTypeAdapter
import smarthome.client.data.retrofit.RuntimeTypeAdapterFactory

abstract class RuntimeDataTypeAdapter<T>(cls: Class<T>) : DataTypeAdapter<T> {
    val factory: RuntimeTypeAdapterFactory<T> = RuntimeTypeAdapterFactory.of(cls)
}
