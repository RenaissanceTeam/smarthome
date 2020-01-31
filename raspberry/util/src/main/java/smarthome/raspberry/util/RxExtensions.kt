package smarthome.raspberry.util

import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

fun testRxSchedulers() {
    RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
    RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
}