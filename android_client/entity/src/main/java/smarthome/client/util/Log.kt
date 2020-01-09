package smarthome.client.util

import android.util.Log

fun log(msg: String, tag: String="shlog") = Log.d(tag, msg)
fun log(thr: Throwable, tag: String="shlog") = Log.w(tag, thr)