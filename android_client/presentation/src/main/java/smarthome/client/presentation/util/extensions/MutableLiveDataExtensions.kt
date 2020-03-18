package smarthome.client.presentation.util.extensions

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.updateWith(block: (T?) -> T?) {
    value = block(value)
}

