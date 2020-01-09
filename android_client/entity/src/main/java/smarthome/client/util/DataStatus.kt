package smarthome.client.util

data class DataStatus<T>(
    val data: T?,
    val status: String
) {
    companion object {
        fun <T> from(value: T?): DataStatus<T> {
            return when (value) {
                null -> DataStatus(null, EMPTY)
                else -> DataStatus(value, DATA)
            }
        }
    }
}

const val DATA = "DATA"
const val EMPTY = "EMPTY"

