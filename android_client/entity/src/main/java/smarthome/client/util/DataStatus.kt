package smarthome.client.util

sealed class DataStatus<T> {
    companion object{
        fun <T> from(value: T?): DataStatus<T> {
            return when (value) {
                null -> EmptyStatus()
                else -> Data(value)
            }
        }
    }
}

class Data<T>(val data: T): DataStatus<T>()
class ErrorStatus<T>(val cause: Throwable,
                     val lastData: Data<T>? = null): DataStatus<T>()
class EmptyStatus<T> : DataStatus<T>()
class LoadingStatus<T>(val lastData: Data<T>? = null) : DataStatus<T>()

val <T>DataStatus<T>.data: T? get() {
    return when (this) {
        is Data -> this.data
        is ErrorStatus -> this.lastData?.data
        is LoadingStatus -> this.lastData?.data
        else -> null
    }
}
