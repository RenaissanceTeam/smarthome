package smarthome.client.data.api.typeadapter

interface DataTypeAdapter<T> {
    fun setTypes(types: List<Class<out T>>)
}