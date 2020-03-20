package smarthome.client.presentation.util.extensions

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun <T> MutableLiveData<T>.updateWith(block: (T?) -> T?) {
    value = block(value)
}

inline fun <T, R> T.runInScopeLoading(scope: CoroutineScope,
                                      loading: MutableLiveData<Boolean>,
                                      crossinline block: suspend T.() -> R): Job {
    return scope.launch {
        loading.updateWith { true }
        block()
        loading.updateWith { false }
    }
}
