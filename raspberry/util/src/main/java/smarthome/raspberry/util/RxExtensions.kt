package smarthome.raspberry.util

import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

fun <T : Any> Observable<T>.subscribeOnUiBy(
    onError: (Throwable) -> Unit = {} ,
    onComplete: () -> Unit = {},
    onNext: (T) -> Unit = {}
): Disposable  {
    return this.subscribeOn(AndroidSchedulers.mainThread()).subscribeBy(onError, onComplete, onNext)
}

fun testRxSchedulers() {
    RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
    RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
    RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
}