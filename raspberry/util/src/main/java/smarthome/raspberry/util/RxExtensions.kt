package smarthome.raspberry.util

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

fun <T : Any> Observable<T>.subscribeOnUiBy(
    onError: (Throwable) -> Unit = {} ,
    onComplete: () -> Unit = {},
    onNext: (T) -> Unit = {}
): Disposable  {
    return this.subscribeOn(AndroidSchedulers.mainThread()).subscribeBy(onError, onComplete, onNext)
}

